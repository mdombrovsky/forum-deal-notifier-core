package post

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        fixedRateTimer = createFixedRateTimerForRefresh(refreshIntervalSeconds)
    }

    fun stopPolling() {
        fixedRateTimer?.cancel()
        fixedRateTimer = null
    }

    fun isPolling(): Boolean {
        return fixedRateTimer != null
    }

    /* creates a timer that executes generateNotificationTask once time is up */
    private fun createFixedRateTimerForRefresh(secondsInterval: Int): Timer {
        return (
                fixedRateTimer(
                    "forum-deal-notifier-core-Refresh-Timer",
                    false,
                    0,
                    secondsInterval * 1000.toLong()
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        process()
                    }
                }
                )
    }

    suspend fun process(debugUseOldPosts: Boolean = false) {
        mutex.withLock {
            for (postFinder in postFinders.values) {
                postFinder.process(debugUseOldPosts)
            }
        }
    }

    override fun getPostFinder(scraper: Scraper, runBlocking: Boolean): PostFinder {
        return if (postFinders.containsKey(scraper)) {
            postFinders[scraper]!!
        } else {
            val newPostFinder = PostFinder(scraper)
            val addPostFinderTask: suspend () -> Unit = {
                // Prevent modify while iterating error
                mutex.withLock {
                    postFinders[scraper] = newPostFinder
                }
            }
            when {
                runBlocking -> {
                    runBlocking {
                        addPostFinderTask.invoke()
                    }
                }

                else -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        addPostFinderTask.invoke()
                    }
                }
            }
            newPostFinder
        }
    }
}