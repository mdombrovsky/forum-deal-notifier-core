package notification.batch

import notification.Notifier
import user.User
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.withLock

/**
 * This class is meant to batch multiple user messages into one to help with rate limits
 *
 * This uses a tick timer to check the firstUpdated time of every user,
 * to track how long to wait for additional messages
 */
class BatchingNotifier(
    private val delegate: Notifier,
    private val batchTime: Long = 2500L
) : Notifier {

    private data class UserMessages(
        val messages: MutableList<String>,
        val firstUpdated: Long
    )

    private val messageQueue: MutableMap<User, UserMessages> = mutableMapOf()
    private val lock = ReentrantLock()

    init {
        // Tick every 1 second, flush stale messages
        fixedRateTimer(
            name = "forum-deal-notifier-core-BatchingNotifier-Tick",
            daemon = true,
            initialDelay = 1000L,
            period = 1000L
        ) {
            checkSendMessages()
        }
    }

    private fun checkSendMessages() {
        val now = System.currentTimeMillis()
        val messagesToSend: Map<User, List<String>> = lock.withLock {
            val messagesReadyToSend = messageQueue.filter { (_, userMessages) ->
                now - userMessages.firstUpdated >= batchTime
            }

            // Remove stale entries and prepare their messages for flushing
            messagesReadyToSend.mapValues { it.value.messages.toList() }.also {
                messagesReadyToSend.keys.forEach { user -> messageQueue.remove(user) }
            }
        }

        // Send messages outside the lock
        messagesToSend.forEach { (user, messages) ->
            if (messages.isNotEmpty()) {
                delegate.message(user, messages.joinToString("\n\n"))
            }
        }
    }

    override fun message(user: User, message: String) {
        val now = System.currentTimeMillis()
        lock.withLock {
            if (messageQueue.contains(user)) {
                messageQueue[user]!!.messages.add(message)
            } else {
                messageQueue[user] = UserMessages(mutableListOf(message), now)
            }
        }
    }
}
