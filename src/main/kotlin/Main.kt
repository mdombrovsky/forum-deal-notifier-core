import notification.TelegramNotifier
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
    val user = User("m", telegramUserId)

    val bufferedReader2: BufferedReader = File("telegram.api").bufferedReader()
    val apiKey = bufferedReader2.use { it.readText() }.trim()
    TelegramNotifier(apiKey).notify(user, scraper.getAllPosts()[0])
}