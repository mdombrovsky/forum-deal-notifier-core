package examples

import notification.stdout.PrintlnNotifier
import post.PostFinderManager
import scraper.Scraper
import scraper.custom.RFDNewScraper
import user.User

fun main() {
    val manager = PostFinderManager()

    val scraper: Scraper = RFDNewScraper()

    val notifier = PrintlnNotifier()


    val user = User("John")

    user.queriesManager.addSimpleQuery(
        queryString = "(North Face | McMurdo) & parka",
        postFinder = manager.getPostFinder(scraper),
        notifier = notifier,
        user = user,
        queryTitle = "North Face Parka"
    )

    user.queriesManager.addSimpleQuery(
        queryString = "amazon",
        postFinder = manager.getPostFinder(scraper),
        notifier = notifier,
        user = user,
        queryTitle = "All Amazon deals"
    )

    manager.startPollingForNewPosts()
}