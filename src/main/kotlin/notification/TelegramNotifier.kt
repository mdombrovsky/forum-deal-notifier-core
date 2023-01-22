package notification

import Post
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId

class TelegramNotifier(val apiKey: String) : Notifier<TelegramId> {
    val bot = bot {
        token = apiKey
    }

    override suspend fun notify(id: TelegramId, post: Post) {
        bot.sendMessage(chatId = ChatId.fromId(id.value.toLong()), post.toPrettyString())
    }
}