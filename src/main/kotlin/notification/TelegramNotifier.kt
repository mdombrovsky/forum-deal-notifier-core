package notification

import Post
import Query
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId
import user.User

class TelegramNotifier(val apiKey: String) : Notifier {
    val bot = bot {
        token = apiKey
    }

    override suspend fun notify(user: User, query: Query, post: Post) {
        val id = user.retrieveCredential(TelegramId::class.java)
        if (id == null) {
            throw UnsupportedOperationException("Telegram notifications not registered with user")
        } else {
            bot.sendMessage(
                chatId = ChatId.fromId(id.value.toLong()),
                post.toPrettyString()
            )
        }
    }
}