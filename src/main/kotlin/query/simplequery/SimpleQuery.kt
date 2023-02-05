package query.simplequery

import Post
import query.Query

/***
 * todo either use on success function or handle notifications direct from user class
 */
class SimpleQuery(
    override var title: String = "",
    criteriaInput: ArrayList<Criteria>
) : Query {


    private val criteriaArrayList: ArrayList<Criteria> = criteriaInput
    private val criteria: List<Criteria> = criteriaArrayList
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

    override fun matches(post: Post): Boolean {
        for (criteria: Criteria in this.criteriaArrayList) {
            if (!criteria.matches(post)) {
                return false
            }
        }
        return true

    }
}