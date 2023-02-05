package query.universal

import Post
import query.Query

class MatchAll(override var title: String = "Match all posts") : Query {
    override fun getSearchTerms(): String = "ALL"
    override fun matches(post: Post): Boolean = true
}