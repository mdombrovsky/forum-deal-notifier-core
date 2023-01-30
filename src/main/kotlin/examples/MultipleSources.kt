import notification.stdout.PrintlnNotifier
import post.PostFinder
import query.universal.MatchAll
import scraper.RSSScraper
import scraper.Scraper
import scraper.custom.RFDScraper
import user.User


suspend fun main() {
    // This uses SlickDeals RSS feed to alert you of new deals
    val scraper1: Scraper =
        RSSScraper("https://slickdeals.net/newsearch.php?mode=frontpage&searcharea=deals&searchin=first&rss=1")
    val scraper2: Scraper = RFDScraper()
    val user = User("John")

    val postFinder1 = PostFinder(scraper1)
    val postFinder2 = PostFinder(scraper2)

    val notifier = PrintlnNotifier()

    // MatchAll is a simple query that matches all posts, mostly used for debugging
    user.queriesManager.addQuery(
        query = MatchAll(),
        postFinder = postFinder1,
        notifier = notifier,
        user = user
    )

    // MatchAll is a simple query that matches all posts, mostly used for debugging
    user.queriesManager.addQuery(
        query = MatchAll(),
        postFinder = postFinder2,
        notifier = notifier,
        user = user
    )
    
    // This will refresh the rss feed for new posts every 60 seconds
    Manager(60).apply {
        register(postFinder1)
        register(postFinder2)
        startPolling()
    }
}