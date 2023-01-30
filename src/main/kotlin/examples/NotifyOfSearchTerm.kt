import notification.telegram.TelegramId
import notification.telegram.TelegramNotifier
import post.PostFinder
import scraper.Scraper
import scraper.custom.RFDScraper
import user.User

/**
 * args:
 *      args[0]: telegram api key
 *      args[1]: telegram user message id
 */
suspend fun main(args: Array<String>) {
    val scraper: Scraper = RFDScraper()

    val telegramNotifier = TelegramNotifier(args[0])

    val user = User("John").apply { registerCredential(TelegramId(args[1])) }

    val postFinder = PostFinder(scraper)

    user.queriesManager.addSimpleQuery(
        queryString = "(North Face | McMurdo) & parka",
        postFinder = postFinder,
        notifier = telegramNotifier,
        user = user,
        queryTitle = "North Face Parka"
    )

    user.queriesManager.addSimpleQuery(
        queryString = "amazon",
        postFinder = postFinder,
        notifier = telegramNotifier,
        user = user,
        queryTitle = "All Amazon deals"
    )

    Manager(60).apply {
        register(postFinder)
        startPolling()
    }
}