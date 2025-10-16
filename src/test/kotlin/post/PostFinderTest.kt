package post

import Post
import kotlinx.coroutines.runBlocking
import notification.Notifier
import org.junit.jupiter.api.Test
import query.universal.MatchAll
import scraper.Scraper
import scraper.ScraperConfig
import user.User

class PostFinderTest {
    @Test
    fun alertOfAll() {
        var notificationCount = 0
        val notifier = object : Notifier {
            override fun message(user: User, message: String) {
                notificationCount++
            }
        }
        var postFinderCalledCount = 0
        val postFinder = PostFinder(object : Scraper(ScraperConfig()) {
            override fun equals(other: Any?): Boolean {
                TODO("Not yet implemented")
            }

            override fun hashCode(): Int {
                TODO("Not yet implemented")
            }

            override suspend fun getAllPosts(): List<Post> {
                return if (postFinderCalledCount == 0) {
                    postFinderCalledCount++
                    // Posts to throw out because they are old
                    listOf(
                        Post("myTitle", "https://www.myURL1.com", "mySource"),
                        Post("myTitle", "https://www.myURL2.com", "mySource"),
                        Post("myTitle", "https://www.myURL3.com", "mySource"),
                        Post("myTitle", "https://www.myURL4.com", "mySource"),
                        Post("myTitle", "https://www.myURL5.com", "mySource")
                    )
                } else {
                    postFinderCalledCount++
                    // Posts to potentially display
                    listOf(
                        Post("myTitle", "https://www.myURL6.com", "mySource"),
                        Post("myTitle", "https://www.myURL7.com", "mySource"),
                        Post("myTitle", "https://www.myURL3.com", "mySource"),
                        Post("myTitle", "https://www.myURL4.com", "mySource"),
                    )
                }
            }
        })
        val user = User("Joe")
        user.queriesManager.addQuery(MatchAll(), postFinder, user, notifier)
        runBlocking {
            postFinder.process()
            // throw out all old posts on load
            assert(notificationCount == 0)
            postFinder.process()
            assert(notificationCount == 2)
        }
        assert(postFinderCalledCount == 2)
    }
}