package post

import Post
import notification.Notifier
import query.ManagedQuery
import query.Query
import query.getQueryFoundTitle
import scraper.Scraper
import user.User

class PostFinder internal constructor(val scraper: Scraper) {
    private val userQueries: HashMap<User, HashSet<ManagedQuery>> = HashMap()
    private var isInitialized: Boolean = false


    internal suspend fun process(debugUseOldPosts: Boolean = false) {
        when {
            userQueries.isEmpty() -> {
                // Don't waste resources fetching if there is no point
            }

            debugUseOldPosts -> {
                // Debug statement to alert of every single post it can get
                alertUsersOfPosts(scraper.getAllPosts())
            }

            !isInitialized -> {
                // Make sure to initialize the scraper so that only new posts are alerted off next time
                scraper.getNewPosts()
                isInitialized = true
            }

            else -> {
                // Let's alert the users of new posts now
                alertUsersOfPosts(scraper.getNewPosts())
            }
        }
    }

    private fun alertUsersOfPosts(posts: List<Post>) {
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

                notifier.notify(user, post, matchedQueries[triple]!!.getQueryFoundTitle())
            }
        }
    }

    internal fun registerQuery(query: ManagedQuery) {
        val user = query.user
        if (!userQueries.containsKey(user)) {
            userQueries[user] = HashSet()
        }
        userQueries[user]!!.add(query)
    }

    internal fun deregisterQuery(query: ManagedQuery) {
        val user = query.user
        userQueries[user]!!.remove(query)
        if (userQueries[user]!!.isEmpty()) {
            userQueries.remove(user)
        }

        // If all queries are deleted the state will no longer be refreshed, and not be current
        if (userQueries.isEmpty()) {
            isInitialized = false
        }

    }
}