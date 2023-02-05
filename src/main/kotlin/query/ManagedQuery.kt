package query

import Post
import notification.Notifier
import post.PostFinder
import user.User

class ManagedQuery(
    val query: Query,
    val user: User,
    val postFinder: PostFinder,
    val notifier: Notifier,
    enabled: Boolean = false
) : Query {
    var enabled: Boolean = enabled
        set(value) {
            if (value) {
                postFinder.registerQuery(this)
            } else {
                postFinder.deregisterQuery(this)
            }
            field = value
        }

    init {
        if (enabled) {
            postFinder.registerQuery(this)
        }
    }

    override var title: String = query.title

    override fun matches(post: Post): Boolean {
        return query.matches(post)
    }
}