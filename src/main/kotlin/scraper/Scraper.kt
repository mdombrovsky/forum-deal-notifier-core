package scraper

import Post
import utils.MaxSizeHashSet
import java.io.Serializable

abstract class Scraper(rememberMaxPosts: Int = 1000) : Serializable {

    private val seenPosts: MaxSizeHashSet<Post> = MaxSizeHashSet(rememberMaxPosts)

    /**
     * Gets all the posts
     */
    abstract suspend fun getAllPosts(): List<Post>

    /**
     * Gets all the new posts that have appeared after this function was last called
     */
    suspend fun getNewPosts(): List<Post> {
        return seenPosts.createTrimmedList(getAllPosts())
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
        seenPosts.clear()
    }

    /**
     * Get the configuration string so the scraper can be rebuilt
     */
    abstract fun getConfig(): String


    /**
     * Verify that the scraper is working properly
     */
    suspend fun verify(): Boolean {
        return getAllPosts().isNotEmpty()
    }
}