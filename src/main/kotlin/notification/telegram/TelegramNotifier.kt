package notification.telegram

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId
import notification.Notifier
import user.User

class TelegramNotifier(private val apiKey: String) : Notifier {
    private val bot = bot {
        token = apiKey
    }

    override fun message(user: User, message: String) {
        val id = user.retrieveCredential(TelegramId::class.java)
        if (id == null) {
            throw UnsupportedOperationException("Telegram notifications not registered with user")
        } else {
            bot.sendMessage(
                chatId = ChatId.fromId(id.value.toLong()),
                text = message
            )
        }
    }
}