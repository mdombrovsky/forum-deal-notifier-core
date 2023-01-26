package notification.debug

import Post
import notification.Notifier
import user.User

class PrintlnNotifier : Notifier {
    override suspend fun notify(user: User, post: Post, title: String) {
        println("Matched post: ${post.toPrettyString()}\n")
    }
}