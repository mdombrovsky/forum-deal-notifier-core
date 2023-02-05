import notification.stdout.PrintlnNotifier
import post.PostFinderManager
import query.universal.MatchAll
import scraper.RSSScraper
import scraper.Scraper
import user.User
import java.net.URL


suspend fun main() {
    val manager = PostFinderManager()

    // This uses SlickDeals RSS feed to alert you of new deals
    val scraper: Scraper =
        RSSScraper(URL("https://slickdeals.net/newsearch.php?mode=frontpage&searcharea=deals&searchin=first&rss=1"))

    val user = User("John")

    // MatchAll is a simple query that matches all posts, mostly used for debugging
    user.queriesManager.addQuery(
        query = MatchAll(),
        postFinder = manager.getPostFinder(scraper),
        notifier = PrintlnNotifier(),
        user = user
    )

    // This will refresh the rss feed for new posts every 60 seconds
    manager.startPolling(60)
}