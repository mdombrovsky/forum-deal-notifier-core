package query

import Query
import notification.Notifier
import post.PostSniper
import user.User

class QueriesManager {
    private val queriesList: ArrayList<Query> = ArrayList()
    private val queryData: HashMap<Query, Pair<User, Notifier>> = HashMap()
    val queries: List<Query> = queriesList

    private val associatedScraper: HashMap<Query, PostSniper> = HashMap()


    private suspend fun addQuery(query: Query, user: User, notifier: Notifier): Boolean {
        val toReturn = queriesList.add(query)
        if (toReturn) {
            queryData[query] = Pair(user, notifier)
        }
        return toReturn
    }

    private suspend fun removeQueryAt(index: Int): Query {
        val query = queries[index]
        queryData.remove(query)
        associatedScraper[query]?.deregisterQuery(query)
        associatedScraper.remove(query)
        return queriesList.removeAt(index)
    }

    suspend fun enableQueryAt(index: Int, enabled: Boolean) {
        val query = queries[index]
        query.enabled = enabled
        if (enabled) {
            associatedScraper[query]?.registerQuery(query, queryData[query]!!.first, queryData[query]!!.second)
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
        addQuery(query, postSniper, user, notifier)
    }

    suspend fun addQuery(query: Query, postSniper: PostSniper, user: User, notifier: Notifier) {
        addQuery(query, user, notifier)
        associatedScraper[query] = postSniper
        postSniper.registerQuery(query, user, notifier)
    }
}