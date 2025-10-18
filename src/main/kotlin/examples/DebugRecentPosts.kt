package examples

import notification.stdout.PrintlnNotifier
import post.PostFinderManager
import query.universal.MatchAll
import scraper.Scraper
import scraper.custom.RFDNewScraper
import user.User


suspend fun main() {
    val manager = PostFinderManager()

    val scraper: Scraper = RFDNewScraper()
    val user = User("John")

    user.queriesManager.addQuery(
        query = MatchAll(),
        postFinder = manager.getPostFinder(scraper, runBlocking = true),
        notifier = PrintlnNotifier(),
        user = user,
    )

    // This will search the rss feed once, including old posts
    manager.process(debugUseOldPosts = true)
}