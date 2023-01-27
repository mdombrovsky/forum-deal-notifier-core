package query.universal

import Post
import query.Query

class MatchNone(override var title: String, override var enabled: Boolean) : Query {
    override suspend fun matches(post: Post): Boolean {
        return false
    }
}