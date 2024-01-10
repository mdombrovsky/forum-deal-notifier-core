package scraper.custom


import Post
import org.jsoup.nodes.Element
import scraper.HtmlWebScraper
import scraper.getTimeZoneOffset
import java.text.SimpleDateFormat
import java.util.*


class RFDNewScraper : HtmlWebScraper("", "https://forums.redflagdeals.com/hot-deals-f9/?st=0&rfd_sk=tt&sd=d") {
    override fun getPostElementsFromDocument(document: Element): List<Element> {
        return document.getElementsByClass("thread_info_title")
    }

    override fun createPostFromElement(htmlPost: Element): Post {
        val aTags = htmlPost.getElementsByTag("h3")[0].getElementsByTag("a")

        //Note: This has been a source of bugs in the past because it would not account for daylight saving time, I have attempted to remedy it
        //The regex is there for greatly simplifying date parsing
        val dateString: String = htmlPost.getElementsByClass("first-post-time").text()
            .replace("(?<=\\d)(rd|st|nd|th)\\b,".toRegex(), "") + " ${getTimeZoneOffset()}"


        val sourceATag = aTags.getOrNull(aTags.lastIndex - 1)

        val titleATag = aTags.last()
        val id = titleATag!!.attr("href").substringAfterLast("-")

        val simpleDateFormat = SimpleDateFormat("MMM dd yyyy hh:mm a z", Locale.ENGLISH)
        val date = simpleDateFormat.parse(dateString)

        val title = if (sourceATag != null) {
            sourceATag.text() + ": " + titleATag.text()
        } else {
            titleATag.text()
        }
        return Post(
            title = title,
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