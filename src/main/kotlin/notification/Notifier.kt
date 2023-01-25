package notification

import Post
import user.User

interface Notifier {
    suspend fun notify(user: User, post: Post, title: String = "")
}