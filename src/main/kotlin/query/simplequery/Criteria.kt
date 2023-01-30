package query.simplequery

import Post

class Criteria(keywordsInput: ArrayList<Keyword>) {

    private val keywordsArrayList: ArrayList<Keyword> = keywordsInput
    private val keywords: List<Keyword> = keywordsArrayList

    fun matches(post: Post): Boolean {
        for (keyword: Keyword in keywordsArrayList) {
            if (keyword.matches(post)) {
                return true
            }
        }
        return false
    }

    fun getCriteriaDescription(): String {

        val str = StringBuilder()
        if (keywords.isEmpty()) {

            return ""

        }
        if (keywords.size == 1) {

            str.append(keywords[0].text)
            return str.toString()

        }
        str.append("(")
        for (k in keywords) {

            str.append(" " + k.text + " |")

        }
        str.deleteCharAt(str.length - 1)
        str.append(")")

        return str.toString()
    }

}