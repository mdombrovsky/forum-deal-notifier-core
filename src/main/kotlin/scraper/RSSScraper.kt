package scraper

import Post
import SortedPostList
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URL
import java.util.*

class RSSScraper(private val url: URL) : Scraper() {

    private fun getRSSFeed(url: URL): SyndFeed? {
        return try {
            SyndFeedInput().build(XmlReader(url.getInputSteam()))
        } catch (e: Exception) {
            println("URL: ${url.toExternalForm()}, Error getting response: $e")
            null
        }
    }

    override suspend fun getAllPosts(): SortedPostList {
        val posts = SortedPostList()
        getRSSFeed(url)?.let { feed ->
            for (entry in feed.entries) {
                try {
                    posts.add(
                        Post(title = entry.title, url = entry.link, date = entry.publishedDate, source = feed.title)
                    )
                } catch (e: Exception) {
                    println("Unable to capture RSS post, in ${this.getName()}")
                }
            }
        }
        return posts.apply {
            println(
                "Time: ${Date()}, RSS Scraper: ${url.toExternalForm()}, Retrieved ${this.size} posts, last post: ${
                    this.getOrNull(
                        0
                    )?.title
                }"
            )
        }
    }

    override fun getName(): String {
        return "RSS:${url}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RSSScraper

        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }

    override fun getConfig(): String {
        return url.toString()
    }
}