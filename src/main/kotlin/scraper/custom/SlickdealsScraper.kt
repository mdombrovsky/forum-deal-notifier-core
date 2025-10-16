package scraper.custom

import Post
import org.jsoup.nodes.Element
import scraper.HtmlWebScraper
import scraper.ScraperConfig

class SlickdealsScraper(scraperConfig: ScraperConfig = ScraperConfig()) :
    HtmlWebScraper("https://slickdeals.net/", scraperConfig) {
    override fun getPostElementsFromDocument(document: Element): List<Element> {
        return document.select("li.bp-p-sidebarDeals_deal")
    }

    override fun createPostFromElement(htmlPost: Element): Post {
        val titleElement: Element = htmlPost.selectFirst("h4.bp-p-sidebarDeals_title a")!!
        val title = titleElement.text()
        val url = titleElement.attr("href")

        return Post(
            title = title,
            url = url,
            source = getName(),
        )
    }

    override fun getName(): String {
        return "Slickdeals trending posts"
    }
}