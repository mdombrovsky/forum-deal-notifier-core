import notification.stdout.PrintlnNotifier
import post.PostFinder
import query.universal.MatchAll
import scraper.RSSScraper
import scraper.Scraper
import user.User


suspend fun main() {
    // This uses SlickDeals RSS feed to alert you of new deals
    val scraper: Scraper =
        RSSScraper("https://slickdeals.net/newsearch.php?mode=frontpage&searcharea=deals&searchin=first&rss=1")

    val user = User("John")

    val postFinder = PostFinder(scraper)

    // MatchAll is a simple query that matches all posts, mostly used for debugging
    user.queriesManager.addQuery(
        query = MatchAll(),
        postFinder = postFinder,
        notifier = PrintlnNotifier(),
        user = user
    )

    // This will refresh the rss feed for new posts every 60 seconds
    Manager(60).apply {
        register(postFinder)
        startPolling()
    }
}