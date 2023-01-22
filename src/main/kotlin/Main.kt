import scraper.RSSScraper
import scraper.Scraper


suspend fun main(args: Array<String>) {
    val url = "https://forums.redflagdeals.com/feed/forum/9"
    val scraper: Scraper = RSSScraper(url)
    println(scraper.getNewPosts());
    println(scraper.getNewPosts());

}