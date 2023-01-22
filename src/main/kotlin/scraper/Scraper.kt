package scraper

import SortedPostList
import java.io.Serializable

interface Scraper : Serializable {

    /**
     * Gets all the posts, mostly useful for debugging
     * Use @see getNewPosts(): SortedPostList instead
     */
    suspend fun getAllPosts(): SortedPostList

    /**
     * Gets all the new posts that have appeared after this function was last called
     */
    suspend fun getNewPosts(): SortedPostList

    /**
     * The displayable name of this scraper
     */
    fun getName(): String

    /**
     * Need to override this method
     */
    override fun equals(other: Any?): Boolean

    /**
     * Need to override this method
     */
    override fun hashCode(): Int

    /**
     * Resets the state of the scraper
     */
    fun reset()
}