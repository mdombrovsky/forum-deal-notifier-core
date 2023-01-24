package notification

import Post
import Query
import user.User

interface Notifier {
    suspend fun notify(user: User, query: Query, post: Post)
}