package query

import notification.Notifier
import post.PostFinder
import query.simplequery.getSimpleQueryFrom
import user.User

class QueriesManager {
    private val managedQueriesList: ArrayList<ManagedQuery> = ArrayList()

    val queries: List<ManagedQuery> = managedQueriesList

    fun removeQueryAt(index: Int): Query {
        val query = managedQueriesList[index]
        query.enabled = false
        return managedQueriesList.removeAt(index).query
    }

    fun enableQueryAt(index: Int, enabled: Boolean) {
        val query = managedQueriesList[index]
        query.enabled = enabled
    }

    fun addSimpleQuery(
        queryString: String, postFinder: PostFinder, user: User, notifier: Notifier, queryTitle: String = ""
    ) {
        val query = getSimpleQueryFrom(queryString) ?: throw UnsupportedOperationException("query not parsed properly")
        query.title = queryTitle
        addQuery(query, postFinder, user, notifier)
    }

    fun addQuery(query: Query, postFinder: PostFinder, user: User, notifier: Notifier) {
        val managedQuery = ManagedQuery(
            query = query, user = user, postFinder = postFinder, notifier = notifier, enabled = true
        )
        managedQueriesList.add(managedQuery)
    }
}