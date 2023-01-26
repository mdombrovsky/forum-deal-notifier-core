package query


fun List<Query>.getQueryFoundTitle(): String =
    if (isEmpty()) {
        ""
    } else if (size == 1) {
        get(0).getQueryFoundTitle()
    } else {
        val allQueryTitles: StringBuilder = StringBuilder()
        for (i: Int in 0..lastIndex) {
            allQueryTitles.append(get(i).title)
            if (i != lastIndex) {
                allQueryTitles.append(", ")
            }
        }
        "Matched Queries: $allQueryTitles"
    }

fun Query.getQueryFoundTitle(): String =
    "Matched Query: $title"
