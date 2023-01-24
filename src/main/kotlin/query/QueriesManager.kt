package query

import Post
import Query
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import notification.Notifier
import post.PostSniper
import user.User

class QueriesManager {
    private val queriesList: ArrayList<Query> = ArrayList()
    val queries: List<Query> = queriesList

    private val associatedScraper: HashMap<Query, PostSniper> = HashMap()

    private val mutex: Mutex = Mutex()

    private suspend fun addQuery(query: Query): Boolean {
        mutex.withLock {
            return queriesList.add(query)
        }
    }

    private suspend fun removeQueryAt(index: Int): Query {
        val query = queries[index]
        associatedScraper[query]?.deregisterQuery(query)
        associatedScraper.remove(query)
        mutex.withLock {
            return queriesList.removeAt(index)
        }
    }

    suspend fun enableQueryAt(index: Int, enabled: Boolean) {
        val query = queries[index]
        mutex.withLock {
            query.enabled = enabled
        }
        if (enabled) {
            associatedScraper[query]?.registerQuery(query)
        } else {
            associatedScraper[query]?.deregisterQuery(query)
        }
    }

    suspend fun addQuery(
        queryString: String,
        postSniper: PostSniper,
        user: User,
        notifier: Notifier,
        queryTitle: String = ""
    ) {
        val query = getQueryFrom(queryString)
        if (query == null) {
            throw UnsupportedOperationException("query not parsed properly")
        }
        query.title = queryTitle
        query.onMatched = { post: Post ->
            notifier.notify(user, query, post)
        }
        addQuery(query, postSniper)
    }

    suspend fun addQuery(query: Query, postSniper: PostSniper) {
        addQuery(query)
        associatedScraper[query] = postSniper
        postSniper.registerQuery(query)
    }
}