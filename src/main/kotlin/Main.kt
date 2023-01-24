import notification.TelegramId
import notification.TelegramNotifier
import post.PostSniper
import scraper.RSSScraper
import scraper.Scraper
import user.User
import java.io.BufferedReader
import java.io.File


suspend fun main(args: Array<String>) {
    val url = "https://forums.redflagdeals.com/feed/forum/9"
    val scraper: Scraper = RSSScraper(url)

    val bufferedReader: BufferedReader = File("telegram_user.api").bufferedReader()
    val telegramUserId = bufferedReader.use { it.readText() }.trim()
    val user = User("m").apply { registerCredential(TelegramId(telegramUserId)) }

    val bufferedReader2: BufferedReader = File("telegram.api").bufferedReader()
    val apiKey = bufferedReader2.use { it.readText() }.trim()
    val telegramNotifier = TelegramNotifier(apiKey)

    val postSniper = PostSniper(scraper)
    user.queriesManager.addQuery(
        queryString = "Sennheiser",
        postSniper = postSniper,
        notifier = telegramNotifier,
        user = user,
        queryTitle = "Test Query"
    )
    postSniper.process()

}