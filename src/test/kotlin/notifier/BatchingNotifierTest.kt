package notifier

import notification.Notifier
import notification.batch.BatchingNotifier
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import user.User
import java.lang.Thread.sleep
import kotlin.test.assertEquals

class BatchingNotifierTest {

    lateinit var messageMap: MutableMap<User, MutableList<String>>

    inner class TestNotifier : Notifier {
        override fun message(user: User, message: String) {
            messageMap.computeIfAbsent(user) { mutableListOf() }.add(message)
        }
    }

    @BeforeEach
    fun setup() {
        resetMessageMap()
    }

    @Test
    fun canBatchPosts() {
        val message1 = "First"
        val message2 = "Second"
        val message3 = "Third"
        val message4 = "Fourth"

        val user = User("Bob")
        val notifier = BatchingNotifier(TestNotifier(), batchTime = 500L)
        notifier.message(user, message1)
        notifier.message(user, message2)
        notifier.message(user, message3)

        // wait 1000 + 500 for next cycle
        sleep(2000)
        notifier.message(user, message4)
        assertEquals(
            mapOf(
                user to listOf(
                    "$message1\n\n$message2\n\n$message3"
                )
            ), messageMap
        )

        resetMessageMap()
        // wait 1000 + 500 for next cycle
        sleep(2000)
        assertEquals(
            mapOf(
                user to listOf(
                    message4
                )
            ), messageMap
        )

    }

    fun resetMessageMap() {
        messageMap = mutableMapOf()
    }
}