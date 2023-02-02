package examples

import notification.stdout.PrintlnNotifier
import post.PostFinderManager
import scraper.Scraper
import scraper.custom.RedditJSONScraper
import user.User


suspend fun main() {
    val manager = PostFinderManager()

    // This uses SlickDeals RSS feed to alert you of new deals
    val scraper: Scraper =
        RedditJSONScraper("bapcsalescanada")

    val user = User("John")

    // MatchAll is a simple query that matches all posts, mostly used for debugging
    user.queriesManager.addSimpleQuery(
        queryString = "SSD",
        postFinder = manager.getPostFinder(scraper),
        notifier = PrintlnNotifier(),
        user = user,
        queryTitle = "SSD Search"
    )

    // This will search the rss feed once
    manager.process()
}