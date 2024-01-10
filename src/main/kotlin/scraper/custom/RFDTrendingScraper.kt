package scraper.custom


import Post
import org.jsoup.nodes.Element
import scraper.HtmlWebScraper


class RFDTrendingScraper : HtmlWebScraper("", "https://forums.redflagdeals.com/") {
    override fun getPostElementsFromDocument(document: Element): List<Element> {
        return document.getElementsByClass("thread_title")
    }

    override fun createPostFromElement(htmlPost: Element): Post {
        val title = htmlPost.text()
        val webId = htmlPost.attr("href").substringAfterLast("-")
        return Post(
            title = title,
            url = "https://forums.redflagdeals.com/-$webId",
            source = "RedFlagDeals: Trending Hot Deals",
        )
    }


    override fun getName(): String {
        return "RFD trending posts"
    }

    override fun getConfig(): String {
        return ""
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