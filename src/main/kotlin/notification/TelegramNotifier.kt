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

    suspend fun notify(user: User, queries: List<Query>, post: Post) {
        val id = user.retrieveCredential(TelegramId::class.java)
        if (id == null) {
            throw UnsupportedOperationException("Telegram notifications not registered with user")
        } else {
            /**
            TODO refactor this title functionality separate function
            Do this when adding email notifier
             */
            val title: String =
                if (queries.isEmpty()) {
                    ""
                } else if (queries.size == 1) {
                    "Matched Query: ${queries[0].title}"
                } else {
                    val allQueryTitles: StringBuilder = StringBuilder()
                    for (i: Int in 0..queries.lastIndex) {
                        allQueryTitles.append(queries[i].title)
                        if (i != queries.lastIndex) {
                            allQueryTitles.append(", ")
                        }
                    }
                    "Matched Queries: $allQueryTitles"
                }

            notify(id.value.toLong(), title, post.toPrettyString())
        }
    }

    override suspend fun notify(user: User, query: Query, post: Post) {
        notify(user, listOf(query), post)
    }

    suspend fun notify(user: User, post: Post) {
        notify(user, listOf(), post)
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