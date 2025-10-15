package examples

import notification.LongNotifierUserCredentials
import notification.stdout.PrintlnNotifier
import post.PostFinderManager
import query.universal.MatchAll
import scraper.Scraper
import scraper.custom.RFDNewScraper
import user.User


suspend fun main() {
    val manager = PostFinderManager()

    val scraper: Scraper = RFDNewScraper()

    val user = User("m").apply { registerCredential(LongNotifierUserCredentials(1L)) }

    val notifier = PrintlnNotifier()

    user.queriesManager.addSimpleQuery(
        queryString = "Lenovo",
        postFinder = manager.getPostFinder(scraper),
        notifier = notifier,
        user = user,
        queryTitle = "Test Query (Lenovo)"
    )
    user.queriesManager.addSimpleQuery(
        queryString = "128gb",
        postFinder = manager.getPostFinder(scraper),
        notifier = notifier,
        user = user,
        queryTitle = "Test Query 2 (128gb)"
    )
    user.queriesManager.addSimpleQuery(
        queryString = "a | b",
        postFinder = manager.getPostFinder(scraper),
        notifier = PrintlnNotifier(),
        user = user,
        queryTitle = "All posts with a or b"
    )
    user.queriesManager.addQuery(
        query = MatchAll(),
        postFinder = manager.getPostFinder(scraper),
        notifier = PrintlnNotifier(),
        user = user
    )

    manager.process()
}