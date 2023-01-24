package notification

import Post
import Query
import user.User

interface Notifier {
    suspend fun notify(user: User, queries: List<Query>, post: Post)
    suspend fun notify(user: User, query: Query, post: Post)
    suspend fun notify(user: User, post: Post)
}