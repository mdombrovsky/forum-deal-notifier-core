package post

import Post
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class SortedPostListTest {
    private val date1 = Date(1597435540000)
    private val date2 = Date(1597521940000)
    private val date3 = Date(1597608340000)
    private val date4 = Date(1597651540000)
    private val date5 = Date(1597694740000)
    private val date6 = Date(1597696168000)
    private val date7 = Date(1597696268000)
    private val date8 = Date(1597696368000)
    private val date9 = Date(1597696468000)

    private val post1 = Post(title = "1", date = date1, url = "1")
    private val post2 = Post(title = "2", date = date2, url = "2")
    private val post3 = Post(title = "3", date = date3, url = "3")
    private val post4 = Post(title = "4", date = date4, url = "4")
    private val post5 = Post(title = "5", date = date5, url = "5")
    private val post6 = Post(title = "6", date = date6, url = "6")
    private val post7 = Post(title = "7", date = date7, url = "7")
    private val post8 = Post(title = "8", date = date8, url = "8")
    private val post9 = Post(title = "9", date = date9, url = "9")

    // Posts with same dates, but different titles, meant to simulate unknown/duplicate dates
    private val post9_v2 = Post(title = "9 v2", date = date9, url = "9 v2")


    @Test
    fun testInsertion() {

        val sortedPostList = SortedPostList()
        sortedPostList.add(post7)

        try {
            sortedPostList.add(1, post2)
        } catch (e: UnsupportedOperationException) {

        }
        sortedPostList.add(post4)
        sortedPostList.add(post9_v2)
        sortedPostList.addAll(arrayListOf(post8, post9, post8))

        assertArrayEquals(
            sortedPostList.toArray(),
            arrayOf(post9, post9_v2, post8, post7, post4)
        )
    }


    @Test
    fun testIndexOf() {
        val sortedPostList = SortedPostList()

        sortedPostList.addAll(arrayListOf(post1, post2, post3, post4, post5, post6, post7, post8))

        assertEquals(sortedPostList.indexOf(post3), 5)
    }

    @Test
    fun testReset1() {
        val sortedPostList = SortedPostList()

        sortedPostList.addAll(
            arrayListOf(
                post1,
                post2,
                post9,
                post3,
                post4,
                post5,
                post6,
                post7,
                post8
            )
        )

        sortedPostList.reset()

        assertArrayEquals(sortedPostList.toArray(), arrayOf())
    }

    @Test
    fun testReset2() {
        val sortedPostList = SortedPostList()

        sortedPostList.reset()

        assertArrayEquals(sortedPostList.toArray(), arrayOf())
    }
}