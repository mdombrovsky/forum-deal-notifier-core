package notification

import Post
import user.User

interface Notifier {
    /**
     * This is meant to send a string message directly to the user
     *
     * @param user The user to send the message to
     * @param message The message to send to the user
     */
    fun message(user: User, message: String)

    /**
     * This is meant to send a user-friendly notification to the user
     *
     * @param user The user to send notify
     * @param post The post to notify the user of
     * @param title The title of the notification
     */
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