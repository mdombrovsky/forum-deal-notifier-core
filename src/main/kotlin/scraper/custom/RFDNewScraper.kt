package scraper.custom


import Post
import org.jsoup.nodes.Element
import scraper.HtmlWebScraper
import java.text.SimpleDateFormat
import java.util.*


class RFDNewScraper : HtmlWebScraper("", "https://forums.redflagdeals.com/hot-deals-f9/?rfd_sk=tt&sd=d&sk=tt") {
    override fun getPostElementsFromDocument(document: Element): List<Element> {
        return document.getElementsByClass("thread_main")
    }

    override fun createPostFromElement(htmlPost: Element): Post {
        /*
            Old method of date getting for reference

            //Note: This has been a source of bugs in the past because it would not account for daylight saving time, I have attempted to remedy it
            //The regex is there for greatly simplifying date parsing
            val dateString: String = htmlPost.getElementsByClass("first-post-time").text()
                .replace("(?<=\\d)(rd|st|nd|th)\\b,".toRegex(), "") + " ${getTimeZoneOffset()}"


            val simpleDateFormat = SimpleDateFormat("MMM dd yyyy hh:mm a z", Locale.ENGLISH)
            val date = simpleDateFormat.parse(dateString)
         */

        val dateTimeString = htmlPost.select("time").last()!!.attr("datetime")
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH)
        val date = simpleDateFormat.parse(dateTimeString)

        val sourceTag = htmlPost.selectFirst("a.pill.thread_dealer span")
        val titleTag = htmlPost.selectFirst("h3.thread_title a.thread_title_link")
        val id = titleTag!!.attr("href").substringAfterLast("-")

        val postTitle = if (sourceTag != null) {
            sourceTag.text() + ": " + titleTag.text()
        } else {
            titleTag.text()
        }
        return Post(
            title = postTitle,
            url = "https://forums.redflagdeals.com/-$id",
            source = "RedFlagDeals: Hot Deals",
            date = date
        )
    }

    override fun getName(): String {
        return "RFD new posts"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.name.hashCode()
    }
}