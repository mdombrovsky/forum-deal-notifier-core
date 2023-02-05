package scraper

import SortedPostList
import java.io.Serializable
import java.util.*

abstract class Scraper : Serializable {

    private var mostRecentPostDate: Date? = null

    /**
     * Gets all the posts
     */
    abstract suspend fun getAllPosts(): SortedPostList

    /**
     * Gets all the new posts that have appeared after this function was last called
     */
    suspend fun getNewPosts(): SortedPostList {
        return getAllPosts().also {
            it.removeAllOlderThan(mostRecentPostDate)
            if (it.isNotEmpty()) {
                mostRecentPostDate = it[0].date
            }
        }
    }

    /**
     * The displayable name of this scraper
     */
    abstract fun getName(): String

    /**
     * Need to override this method
     */
    abstract override fun equals(other: Any?): Boolean

    /**
     * Need to override this method
     */
    abstract override fun hashCode(): Int

    /**
     * Resets the state of the scraper
     */
    fun reset() {
        mostRecentPostDate = null
    }

    /**
     * Get the configuration string so the scraper can be rebuilt
     */
    abstract fun getConfig(): String

}