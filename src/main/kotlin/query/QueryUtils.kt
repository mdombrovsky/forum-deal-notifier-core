package query


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
