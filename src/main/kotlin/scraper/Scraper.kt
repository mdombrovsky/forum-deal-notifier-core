package scraper

import Post
import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream
import utils.MaxSizeHashSet
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.zip.GZIPInputStream
import java.util.zip.InflaterInputStream

abstract class Scraper(private val scraperContext: ScraperContext) :
    Serializable {

    private val seenPosts: MaxSizeHashSet<Post> = MaxSizeHashSet(scraperContext.rememberMaxPosts)

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

    protected fun URL.getInputStream(): InputStream {
        val connection = this.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        // Set request headers from your scraper context
        scraperContext.getRequestProps().forEach { (key, value) ->
            connection.setRequestProperty(key, value)
        }

        // Important: accept gzip
        connection.setRequestProperty("Accept-Encoding", "br, gzip, deflate")

        // Detect content encoding from the response
        val encoding = connection.contentEncoding?.lowercase() ?: ""
        val rawStream = BufferedInputStream(connection.inputStream)

        return when {
            encoding.contains("br") -> BrotliCompressorInputStream(rawStream)
            encoding.contains("gzip") -> GZIPInputStream(rawStream)
            encoding.contains("deflate") -> InflaterInputStream(rawStream)
            else -> rawStream
        }
    }

    protected fun getData(urlString: String): String {
        val url = URL(urlString)

        return url.getData()
    }

    protected fun URL.getData(): String {
        return try {
            this.getInputStream().use { inputStream ->
                inputStream.bufferedReader().readText()
            }
        } catch (e: Exception) {
            println("URL: ${this.host}, Error getting response: $e")
            ""
        }
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