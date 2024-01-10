import notification.telegram.TelegramId
import notification.telegram.TelegramNotifier
import post.PostFinderManager
import scraper.Scraper
import scraper.custom.RFDNewScraper
import user.User

/**
 * args:
 *      args[0]: telegram api key
 *      args[1]: telegram user message id
 */
suspend fun main(args: Array<String>) {
    val manager = PostFinderManager()

    val scraper: Scraper = RFDNewScraper()

    val telegramNotifier = TelegramNotifier(args[0])

    val user = User("John").apply { registerCredential(TelegramId(args[1])) }

    user.queriesManager.addSimpleQuery(
        queryString = "(North Face | McMurdo) & parka",
        postFinder = manager.getPostFinder(scraper),
        notifier = telegramNotifier,
        user = user,
        queryTitle = "North Face Parka"
    )

    user.queriesManager.addSimpleQuery(
        queryString = "amazon",
        postFinder = manager.getPostFinder(scraper),
        notifier = telegramNotifier,
        user = user,
        queryTitle = "All Amazon deals"
    )

    manager.startPollingForNewPosts()
}