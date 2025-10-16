package scraper

import Post
import utils.MaxSizeHashSet
import java.io.InputStream
import java.io.Serializable
import java.net.MalformedURLException
import java.net.URL

abstract class Scraper(private val scraperConfig: ScraperConfig) :
    Serializable {

    private val seenPosts: MaxSizeHashSet<Post> = MaxSizeHashSet(scraperConfig.rememberMaxPosts)

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
    open fun getName(): String {
        return this::class.java.simpleName
    }

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
    open fun getConfig(): String {
        return ""
    }


    /**
     * Verify that the scraper is working properly
     */
    suspend fun verify(): Boolean {
        return getAllPosts().isNotEmpty()
    }

    protected fun URL.getInputSteam(
    ): InputStream {
        return this.openConnection().apply {
            connectTimeout = 5000
            readTimeout = 5000

            scraperConfig.getRequestProps().forEach { (key, value) ->
                setRequestProperty(key, value)
            }
        }.inputStream
    }

    protected fun getData(urlString: String): String {
        val url = URL(urlString)

        return url.getData()
    }

    protected fun URL.getData(): String {
        val response: String = try {
            this.getInputSteam().bufferedReader().readText()
        } catch (e: Exception) {
            println("URL: ${this.host}, Error getting response: $e")
            ""
        }
        return response
    }


    /**
     * Transforms a string into an url, return null if unable to
     */
    protected fun String.getURL(): URL? {
        return try {
            URL(this)
        } catch (e: MalformedURLException) {
            null
        }
    }

}