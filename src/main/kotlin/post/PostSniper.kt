package post

import Post
import notification.Notifier
import query.Query
import query.getQueryFoundTitle
import scraper.Scraper
import user.User

class PostSniper(val scraper: Scraper) {
    private data class DataQuery(val query: Query, val user: User, val notifier: Notifier)

    private val queryData: HashMap<Query, DataQuery> = HashMap()
    private val userQueries: HashMap<User, HashSet<DataQuery>> = HashMap()


    suspend fun process() {
        if (queryData.isEmpty()) {
            // Don't waste resources fetching if there is no point
            return
        }
        val posts = scraper.getNewPosts()

        for (user in userQueries.keys) {
            // Track when multiple queries match the same post
            val matchedQueries: HashMap<Triple<Post, User, Notifier>, ArrayList<Query>> = HashMap()

            // Find queries that match
            for (dataQuery in userQueries[user]!!) {
                for (post in posts) {
                    if (dataQuery.query.matches(post)) {
                        val key = Triple(post, dataQuery.user, dataQuery.notifier)
                        if (!matchedQueries.containsKey(key)) {
                            matchedQueries[key] = arrayListOf(dataQuery.query)
                        } else {
                            matchedQueries[key]!!.add(dataQuery.query)
                        }
                    }
                }
            }

            // Notify of matched queries
            for (triple in matchedQueries.keys) {
                val post = triple.first
                val user = triple.second
                val notifier: Notifier = triple.third

                notifier.notify(user, post, getQueryFoundTitle(matchedQueries[triple]!!))
            }
        }
    }

    fun registerQuery(query: Query, user: User, notifier: Notifier) {
        val dataQuery = DataQuery(query, user, notifier)
        queryData[query] = dataQuery

        if (!userQueries.containsKey(user)) {
            userQueries[user] = HashSet()
        }
        userQueries[user]!!.add(dataQuery)
    }

    fun deregisterQuery(query: Query) {
        val dataQuery = queryData[query]!!

        queryData.remove(query)
        userQueries[dataQuery.user]!!.remove(dataQuery)
        if (userQueries[dataQuery.user]!!.isEmpty()) {
            userQueries.remove(dataQuery.user)
        }
    }
}