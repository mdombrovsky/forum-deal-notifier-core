package notification

import Post
import user.User

interface Notifier {
    fun message(user: User, message: String)
    fun notify(user: User, post: Post, title: String = "") {
        this.message(
            user, if (title.trim().isNotEmpty()) {
                "${post.toPrettyString()}\n\n${title}"
            } else {
                post.toPrettyString()
            }
        )
    }
}