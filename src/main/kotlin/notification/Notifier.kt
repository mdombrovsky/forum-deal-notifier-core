package notification

import Post

interface Notifier<T : NotifierIdentification> {
    suspend fun notify(id: T, post: Post)
}