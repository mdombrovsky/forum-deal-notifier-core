package post

import Query
import scraper.Scraper

class PostSniper(val scraper: Scraper, queries: List<Query> = listOf()) {
    private val queries: HashSet<Query> = HashSet()

    init {
        this.queries.addAll(queries)
    }

    suspend fun process() {
        if (queries.isEmpty()) {
            // Don't waste resources fetching if there is no point
            return
        }
        val posts = scraper.getNewPosts()

        for (query in queries) {
            for (post in posts) {
                //TODO prevent duplicate matches, for example query 1 and query 2 of user 1 both flag a post
                query.matches(post)
            }
        }
    }

    fun registerQuery(query: Query) {
        queries.add(query)
    }

    fun deregisterQuery(query: Query) {
        queries.remove(query)
    }
}