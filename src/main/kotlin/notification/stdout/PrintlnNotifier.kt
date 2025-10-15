package notification.stdout

import Post
import notification.LongNotifierUserCredentials
import notification.Notifier
import user.User

class PrintlnNotifier : Notifier {
    override fun message(user: User, message: String) {
        println(message)
    }

    override fun notify(user: User, post: Post, title: String) {
        val id = user.retrieveCredential(LongNotifierUserCredentials::class.java)
        val stringBuilder = StringBuilder()
        stringBuilder.append("Matched post: ${post.toPrettyString()}\n")
        if (id != null) {
            stringBuilder.append("UserId: $id\n")
        }
        message(user, stringBuilder.toString())
    }
}