package post

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import scraper.Scraper
import java.util.*
import kotlin.concurrent.fixedRateTimer

class PostFinderManager : PostFinderAllocator {

    private val postFinders: HashMap<Scraper, PostFinder> = HashMap()
    private val mutex: Mutex = Mutex()
    private var fixedRateTimer: Timer? = null


    fun startPollingForNewPosts(refreshIntervalSeconds: Int = 30) {
        stopPolling()
        CoroutineScope(Dispatchers.IO).launch {
            process(discardPosts = true)
        }
        fixedRateTimer = createFixedRateTimerForRefresh(refreshIntervalSeconds)
    }

    fun stopPolling() {
        fixedRateTimer?.cancel()
    }

    /* creates a timer that executes generateNotificationTask once time is up */
    private fun createFixedRateTimerForRefresh(secondsInterval: Int): Timer {
        return (
                fixedRateTimer(
                    "RSS-Sniper-Refresh-Timer",
                    false,
                    secondsInterval * 1000.toLong(),
                    secondsInterval * 1000.toLong()
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        process()
                    }
                }
                )
    }

    suspend fun process(discardPosts: Boolean = false) {
        mutex.withLock {
            for (postFinder in postFinders.values) {
                postFinder.process(discardPosts)
            }
        }
    }

    override fun getPostFinder(scraper: Scraper): PostFinder {
        return if (postFinders.containsKey(scraper)) {
            postFinders[scraper]!!
        } else {
            val newPostFinder = PostFinder(scraper)
            CoroutineScope(Dispatchers.IO).launch {
                // Prevent modify while iterating error
                mutex.withLock {
                    postFinders[scraper] = newPostFinder
                }
            }
            newPostFinder
        }
    }
}