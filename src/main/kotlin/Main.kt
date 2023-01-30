import notification.stdout.PrintlnNotifier
import notification.telegram.TelegramId
import notification.telegram.TelegramNotifier
import post.PostFinder
import query.universal.MatchAll
import scraper.Scraper
import scraper.custom.RFDScraper
import user.User
import java.io.BufferedReader
import java.io.File


suspend fun main(args: Array<String>) {
//    val url = "https://forums.redflagdeals.com/feed/forum/9"
//    val scraper: Scraper = RSSScraper(url)
//    val scraper: Scraper = RedditJSONScraper("all")
    val scraper: Scraper = RFDScraper()

    val bufferedReader: BufferedReader = File("telegram_user.api").bufferedReader()
    val telegramUserId = bufferedReader.use { it.readText() }.trim()
    val user = User("m").apply { registerCredential(TelegramId(telegramUserId)) }

    val bufferedReader2: BufferedReader = File("telegram.api").bufferedReader()
    val apiKey = bufferedReader2.use { it.readText() }.trim()
    val telegramNotifier = TelegramNotifier(apiKey)

    val postFinder = PostFinder(scraper)
    user.queriesManager.addSimpleQuery(
        queryString = "Lenovo",
        postFinder = postFinder,
        notifier = telegramNotifier,
        user = user,
        queryTitle = "Test Query (Lenovo)"
    )
    user.queriesManager.addSimpleQuery(
        queryString = "128gb",
        postFinder = postFinder,
        notifier = telegramNotifier,
        user = user,
        queryTitle = "Test Query 2 (128gb)"
    )
    user.queriesManager.addSimpleQuery(
        queryString = "a | b",
        postFinder = postFinder,
        notifier = PrintlnNotifier(),
        user = user,
        queryTitle = "All posts with a or b"
    )
    user.queriesManager.addQuery(
        query = MatchAll(),
        postFinder = postFinder,
        notifier = PrintlnNotifier(),
        user = user
    )

    val manager = Manager(15).apply {
        register(postFinder)
//        startPolling()
//        stopPolling()
        process()
    }
}