package notification

import Post
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId
import user.User

class TelegramNotifier(private val apiKey: String) : Notifier {
    private val bot = bot {
        token = apiKey
    }

    override suspend fun notify(user: User, post: Post, title: String) {
        val id = user.retrieveCredential(TelegramId::class.java)
        if (id == null) {
            throw UnsupportedOperationException("Telegram notifications not registered with user")
        } else {
            notify(id.value.toLong(), title, post.toPrettyString())
        }
    }

    private suspend fun notify(id: Long, title: String = "", postDescription: String) {
        bot.sendMessage(
            chatId = ChatId.fromId(id),
            text = if (title.trim().isNotEmpty()) {
                "${title}\n\n${postDescription}"
            } else {
                postDescription
            }
        )
    }
}