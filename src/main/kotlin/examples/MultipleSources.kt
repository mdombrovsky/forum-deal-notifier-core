import notification.stdout.PrintlnNotifier
import post.PostFinderManager
import query.universal.MatchAll
import scraper.RSSScraper
import scraper.Scraper
import scraper.custom.RFDNewScraper
import user.User


suspend fun main() {
    val manager = PostFinderManager()

    // This uses SlickDeals RSS feed to alert you of new deals
    val scraper1: Scraper =
        RSSScraper("https://slickdeals.net/newsearch.php?mode=frontpage&searcharea=deals&searchin=first&rss=1")
    val scraper2: Scraper = RFDNewScraper()
    val user = User("John")


    val notifier = PrintlnNotifier()

    // MatchAll is a simple query that matches all posts, mostly used for debugging
    user.queriesManager.addQuery(
        query = MatchAll(),
        postFinder = manager.getPostFinder(scraper1),
        notifier = notifier,
        user = user
    )

    // MatchAll is a simple query that matches all posts, mostly used for debugging
    user.queriesManager.addQuery(
        query = MatchAll(),
        postFinder = manager.getPostFinder(scraper2),
        notifier = notifier,
        user = user
    )

    // This will refresh the rss feed for new posts every 60 seconds
    manager.startPollingForNewPosts(60)
}