package scraper

import Post
import SortedPostList
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URL
import java.util.*

class RSSScraper(private val url: URL) : Scraper() {

    override suspend fun getAllPosts(): SortedPostList {
        val posts = SortedPostList()
        val feed = SyndFeedInput().build(XmlReader(url))
        for (entry in feed.entries) {
            try {
                posts.add(
                    Post(title = entry.title, url = entry.link, date = entry.publishedDate, source = feed.title)
                )
            } catch (e: Exception) {
                println("Unable to capture RSS post, in ${this.getName()}")
            }
        }
        return posts.apply {
            println("Time: ${Date()}, RSS Scraper, Retrieved ${this.size} posts, last post: ${this.getOrNull(0)?.title}")
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