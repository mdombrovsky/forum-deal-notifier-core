package examples

import notification.stdout.PrintlnNotifier
import post.PostFinderManager
import scraper.Scraper
import scraper.custom.RFDScraper
import user.User


suspend fun main() {
    val manager = PostFinderManager()

    // This uses the RFD feed to alert you of new deals
    val scraper: Scraper = RFDScraper()
    val user = User("John")

    // MatchAll is a simple query that matches all posts, mostly used for debugging
    user.queriesManager.addSimpleQuery(
        queryString = "SSD",
        // This uses a mutex to register the post finder under the hood
        // So use option parameter runBlocking only for debug/demo purposes
        postFinder = manager.getPostFinder(scraper, runBlocking = true),
        notifier = PrintlnNotifier(),
        user = user,
        queryTitle = "SSD Search"
    )

    // This will search the rss feed once, including old posts
    manager.process(debugUseOldPosts = true)
}