package notification

import Post
import user.User

interface Notifier<T : NotifierCredentials> {
    suspend fun notify(user: User, post: Post)
}