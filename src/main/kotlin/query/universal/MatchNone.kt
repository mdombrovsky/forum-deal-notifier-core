package query.universal

import Post
import query.Query

class MatchNone(override var title: String = "Match no posts") : Query {
    override fun getSearchTerms(): String = "NONE"
    override fun matches(post: Post): Boolean = false
}