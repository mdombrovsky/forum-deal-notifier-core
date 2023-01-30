package examples

import Manager
import notification.stdout.PrintlnNotifier
import post.PostFinder
import scraper.Scraper
import scraper.custom.RedditJSONScraper
import user.User


suspend fun main() {
    // This uses SlickDeals RSS feed to alert you of new deals
    val scraper: Scraper =
        RedditJSONScraper("bapcsalescanada")

    val user = User("John")

    val postFinder = PostFinder(scraper)

    // MatchAll is a simple query that matches all posts, mostly used for debugging
    user.queriesManager.addSimpleQuery(
        queryString = "SSD",
        postFinder = postFinder,
        notifier = PrintlnNotifier(),
        user = user,
        queryTitle = "SSD Search"
    )

    // This will search the rss feed once
    Manager().apply {
        register(postFinder)
        process()
    }
}