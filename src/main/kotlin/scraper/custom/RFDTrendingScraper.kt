package scraper.custom


import Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import scraper.Scraper
import scraper.getData
import java.net.URL
import java.util.*


class RFDTrendingScraper : Scraper() {
    private val url = URL("https://forums.redflagdeals.com/")
    override suspend fun getAllPosts(): List<Post> {
        return getTrendingPosts().apply {
            println("Time: ${Date()}, RFD Scraper, Retrieved ${this.size} posts, last post: ${this.getOrNull(0)?.title}")
        }
    }

    override fun getName(): String {
        return "RFD Trending"
    }


    private suspend fun getTrendingPosts(): List<Post> {
        val posts = ArrayList<Post>()

        val doc: Document = Jsoup.parse(url.getData())
        val htmlPosts = doc.getElementsByClass("thread_title")

        for (htmlPost: Element in htmlPosts) {
            val post = createRfdPost(htmlPost)
            posts.add(post)
        }

        return posts
    }

    private fun createRfdPost(htmlPost: Element): Post {
        val title = htmlPost.text()
        val webId = htmlPost.attr("href")
        return Post(
            title = title,
            url = "https://forums.redflagdeals.com/$webId",
            source = "RedFlagDeals: Trending Hot Deals",
        )
    }

    override fun getConfig(): String {
        return "trending"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RFDTrendingScraper

        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }
}