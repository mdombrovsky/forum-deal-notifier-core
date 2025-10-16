package scraper

import Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*

abstract class HtmlWebScraper(
    private val url: String,
    private val scraperContext: ScraperContext,
    private val config: String = ""
) : Scraper(scraperContext) {

    init {
        if (url.getURL() == null) {
            throw IllegalArgumentException("Invalid URL: $url")
        }
    }

    abstract fun getPostElementsFromDocument(document: Element): List<Element>
    abstract fun createPostFromElement(htmlPost: Element): Post
    open fun excludePostElement(htmlPost: Element): Boolean {
        return false
    }

    open fun excludePost(post: Post): Boolean {
        return false
    }

    override suspend fun getAllPosts(): List<Post> {
        return parseHtmlForPosts(
            getData(
                url
            )
        ).apply {
            println("Time: ${Date()}, ${getName()}, Retrieved ${this.size} posts, last post: ${this.getOrNull(0)?.title}")
            scraperContext.registerLoadedPostsForLog(
                ScrapeReport(
                    getName(),
                    Date(),
                    this.size,
                    this.getOrNull(0)?.title
                )
            )
        }
    }

    private fun parseHtmlForPosts(html: String): List<Post> {
        val posts: ArrayList<Post> = ArrayList()

        if (html != "") {
            val document = Jsoup.parse(html)

            val postElements = getPostElementsFromDocument(document)

            for (postElement: Element in postElements) {
                if (excludePostElement(postElement)) continue
                val post = createPostFromElement(postElement)
                if (excludePost(post)) continue
                posts.add(post)
            }
        }
        return posts
    }

    override fun getConfig(): String {
        return config
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HtmlWebScraper

        return config == other.config
    }

    override fun hashCode(): Int {
        return config.hashCode()
    }
}