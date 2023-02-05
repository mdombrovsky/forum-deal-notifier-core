package query

import Post

interface Query {
    var title: String
    fun matches(post: Post): Boolean
}