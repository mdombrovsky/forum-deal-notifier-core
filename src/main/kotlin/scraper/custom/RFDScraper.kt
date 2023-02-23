package scraper.custom


import Post
import SortedPostList
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import scraper.Scraper
import scraper.getData
import scraper.getTimeZoneOffset
import java.text.SimpleDateFormat
import java.util.*


class RFDScraper(private val category: Int = 0) : Scraper() {
    //Category: 0 -> All, 9 -> Computers & Electronics
    private val baseURL: String = "https://forums.redflagdeals.com"
    private val dealListURL: String = "/hot-deals-f9"
    private val searchFilterURL: String = "/?st=1&rfd_sk=tt&sd=d"
    private val categoryPrefixURL: String = "&c="
    private val defaultURL =
        baseURL + dealListURL + searchFilterURL + categoryPrefixURL + category.toString()

    override suspend fun getAllPosts(): SortedPostList {
        return getPosts(defaultURL).apply {
            println("Time: ${Date()}, RFD Scraper, Retrieved ${this.size} posts, last post: ${this.getOrNull(0)?.title}")
        }
    }

    override fun getName(): String {
        return "RFD, category = $category"
    }


    /**
     * Gets at least a certain amount of posts if available
     *
     * @param urlString The url in String format from which to get posts
     * @param number The number of posts to get at least
     */
    private suspend fun getPosts(urlString: String, number: Int = 0): SortedPostList {
        val posts = SortedPostList()


        val doc: Document = Jsoup.parse(getData(urlString))
        val htmlPosts = doc.getElementsByClass("thread_info_title")


        for (htmlPost: Element in htmlPosts) {
            val post = createRfdPost(htmlPost)
            posts.add(post)
        }

        if (number > 0) {
            //Gets the url of the next page
            val nextTag = doc.getElementsByClass("pagination_next").firstOrNull()
            if (nextTag != null) {
                //Does recursive calls to go through all the pages
                posts.addAll(
                    getPosts(
                        baseURL + nextTag.attr("href"),
                        number - posts.size
                    )
                )
            }
        }
        return posts
    }

    private fun createRfdPost(htmlPost: Element): Post {
        val aTags = htmlPost.getElementsByTag("h3")[0].getElementsByTag("a")

        //Note: This has been a source of bugs in the past because it would not account for daylight saving time, I have attempted to remedy it
        //The regex is there for greatly simplifying date parsing
        val dateString: String = htmlPost.getElementsByClass("first-post-time").text()
            .replace("(?<=\\d)(rd|st|nd|th)\\b,".toRegex(), "") + " ${getTimeZoneOffset()}"


        val sourceATag = aTags.getOrNull(aTags.lastIndex - 1)

        val titleATag = aTags.last()
        val id = titleATag!!.attr("href")

        val simpleDateFormat = SimpleDateFormat("MMM dd yyyy hh:mm a z", Locale.ENGLISH)
        val date = simpleDateFormat.parse(dateString)

        val title = if (sourceATag != null) {
            sourceATag.text() + ": " + titleATag.text()
        } else {
            titleATag.text()
        }
        return Post(
            title = title,
            url = "https://forums.redflagdeals.com/$id",
            source = "RedFlagDeals: Hot Deals",
            date = date
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RFDScraper

        if (category != other.category) return false

        return true
    }

    override fun hashCode(): Int {
        return category
    }

    override fun getConfig(): String {
        return category.toString()
    }
}