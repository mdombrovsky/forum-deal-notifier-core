package query

import Post

interface Query {
    var title: String
    suspend fun matches(post: Post): Boolean
}