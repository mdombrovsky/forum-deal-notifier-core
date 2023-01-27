package query.simplequery

import Post
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import query.Query

/***
 * todo either use on success function or handle notifications direct from user class
 */
class SimpleQuery(
    override var title: String = "",
    criteriaInput: ArrayList<Criteria> = ArrayList()
) : Query {


    private val mutex: Mutex = Mutex()
    private val criteriaArrayList: ArrayList<Criteria> = criteriaInput
    val criteria: List<Criteria> = criteriaArrayList
    var queryDescription: String = regenDescription()
        private set


    private fun regenDescription(): String {

        val str = StringBuilder()

        for (c in criteria) {
            str.append(c.getCriteriaDescription())
            str.append(" & ")
        }

        return str.removeRange(str.length - 3, str.length - 1).toString()

    }

    override suspend fun matches(post: Post): Boolean {
        mutex.withLock {
            for (criteria: Criteria in this.criteriaArrayList) {
                if (!criteria.matches(post)) {
                    return false
                }
            }
            return true
        }

    }

    suspend fun addCriteria(criteria: Criteria): Boolean {
        mutex.withLock {
            return criteriaArrayList.add(criteria)
        }
    }

    suspend fun removeCriteriaAt(index: Int): Criteria {
        mutex.withLock {
            return criteriaArrayList.removeAt(index)
        }
    }
}