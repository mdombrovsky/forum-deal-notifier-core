package notification

import Post
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId
import user.User

class TelegramNotifier(val apiKey: String) : Notifier {
    val bot = bot {
        token = apiKey
    }

    override suspend fun notify(user: User, post: Post) {
        user.telegramId?.let {
            bot.sendMessage(chatId = ChatId.fromId(it.toLong()), post.toPrettyString())
        }
    }
}