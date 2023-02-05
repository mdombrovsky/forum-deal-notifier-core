package query

import Post

interface Query {
    var title: String
    fun getSearchTerms(): String
    fun matches(post: Post): Boolean
}