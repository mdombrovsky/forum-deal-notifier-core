package notification.stdout

import Post
import notification.Notifier
import user.User

class PrintlnNotifier : Notifier {
    override fun message(user: User, message: String) {
        println("Message: $message")
    }

    override fun notify(user: User, post: Post, title: String) {
        println("Matched post: ${post.toPrettyString()}\n")
    }
}