package query.universal

import Post
import query.Query

class MatchNone(override var title: String = "Match no posts") : Query {
    override suspend fun matches(post: Post): Boolean {
        return false
    }
}