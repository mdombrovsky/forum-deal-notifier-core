package query

import Criteria
import Keyword
import SimpleQuery

// Parses string into query object
fun getSimpleQueryFrom(contents: String): SimpleQuery? {

    val query: Query
    var subStrings: List<String>
    var tempStr: String
    val criteriaList = ArrayList<Criteria>()
    var keywordList = ArrayList<Keyword>()
    var numOpenBrackets: Int
    var numClosedBrackets: Int
    val ands = contents.split("&")
    for (s in ands) {

        if (s.contains("&")) {
            return null
        }

        numOpenBrackets = 0
        numClosedBrackets = 0

        for (c in s) {
            if (c == '(') {
                numOpenBrackets++
            } else if (c == ')') {
                numClosedBrackets++
            }
            if (numClosedBrackets > numOpenBrackets) {
                return null
            }
        }

        if (numOpenBrackets != numClosedBrackets || numClosedBrackets > 1 || numOpenBrackets > 1) {
            return null
        }

        subStrings = s.split("|")

        for (subStr in subStrings) {
            if (subStr.trim().contains("|") || subStr.trim().isEmpty()) {
                return null
            }

            tempStr = subStr.trim().replace("(", "")
            tempStr = tempStr.replace(")", "")

            if (tempStr.trim().isEmpty()) {
                return null
            }

            keywordList.add(Keyword(tempStr.trim()))
        }

        criteriaList.add(Criteria(keywordList))
        keywordList = ArrayList()
    }

    query = SimpleQuery("", criteriaList)
    return query
}

fun getQueryFoundTitle(queries: List<Query>): String =
    if (queries.isEmpty()) {
        ""
    } else if (queries.size == 1) {
        getQueryFoundTitle(queries[0])
    } else {
        val allQueryTitles: StringBuilder = StringBuilder()
        for (i: Int in 0..queries.lastIndex) {
            allQueryTitles.append(queries[i].title)
            if (i != queries.lastIndex) {
                allQueryTitles.append(", ")
            }
        }
        "Matched Queries: $allQueryTitles"
    }


fun getQueryFoundTitle(query: Query): String =
    "Matched Query: ${query.title}"
