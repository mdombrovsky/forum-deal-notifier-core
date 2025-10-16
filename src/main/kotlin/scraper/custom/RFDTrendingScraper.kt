package scraper.custom


import Post
import org.jsoup.nodes.Element
import scraper.HtmlWebScraper
import scraper.ScraperConfig


class RFDTrendingScraper(scraperConfig: ScraperConfig = ScraperConfig()) :
    HtmlWebScraper("https://forums.redflagdeals.com/", scraperConfig) {
    override fun getPostElementsFromDocument(document: Element): List<Element> {
        return document.getElementsByClass("list_item")
    }

    override fun createPostFromElement(htmlPost: Element): Post {
        val titleTag = htmlPost.select("a.thread_title")
        val title = titleTag.text()
        val webId = titleTag.attr("href").substringAfterLast("-")
        return Post(
            title = title,
            url = "https://forums.redflagdeals.com/-$webId",
            source = "RedFlagDeals: Trending Hot Deals",
        )
    }

    override fun excludePostElement(htmlPost: Element): Boolean {
        val score = htmlPost.select("span.thread_vote_count").text().toIntOrNull()
        return score != null && score < 0
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