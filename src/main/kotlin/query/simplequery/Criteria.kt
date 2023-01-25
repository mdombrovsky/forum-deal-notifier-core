package query.simplequery

import Post
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Criteria(keywordsInput: ArrayList<Keyword> = ArrayList()) {

    private val keywordsArrayList: ArrayList<Keyword> = keywordsInput
    val keywords: List<Keyword> = keywordsArrayList

    private val mutex: Mutex = Mutex()

    suspend fun matches(post: Post): Boolean {
        mutex.withLock {
            for (keyword: Keyword in keywordsArrayList) {
                if (keyword.matches(post)) {
                    return true
                }
            }
        }
        return false
    }

    suspend fun addKeyword(keyword: Keyword): Boolean {
        mutex.withLock {
            return keywordsArrayList.add(keyword)
        }
    }

    suspend fun removeKeywordAt(index: Int): Keyword {
        mutex.withLock {
            return keywordsArrayList.removeAt(index)
        }
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