package query.universal

import Post
import query.Query

class MatchAll(override var title: String = "Match all posts") : Query {
    override suspend fun matches(post: Post): Boolean {
        return true
    }
}