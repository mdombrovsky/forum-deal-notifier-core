package query

import notification.Notifier
import post.PostFinder
import user.User

class ManagedQuery(
    val query: Query,
    val user: User,
    val postFinder: PostFinder,
    val notifier: Notifier,
    enabled: Boolean = false
) {
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
}