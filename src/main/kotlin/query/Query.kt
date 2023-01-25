package query

import Post

interface Query {
    var title: String
    var enabled: Boolean
    suspend fun matches(post: Post): Boolean
}