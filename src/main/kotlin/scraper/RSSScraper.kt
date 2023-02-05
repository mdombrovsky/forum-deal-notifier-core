package scraper

import Post
import SortedPostList
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URL

class RSSScraper(url: String) : Scraper() {
    private val url = URL(url)

    override suspend fun getAllPosts(): SortedPostList {
        val posts = SortedPostList()
        val feed = SyndFeedInput().build(XmlReader(url))
        for (entry in feed.entries) {
            posts.add(
                Post(title = entry.title, url = entry.link, date = entry.publishedDate, source = feed.title)
            )
        }
        return posts
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