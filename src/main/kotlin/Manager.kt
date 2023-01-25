import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import post.PostSniper
import java.util.*
import kotlin.concurrent.fixedRateTimer

class Manager(private val refreshIntervalSeconds: Int = 30) {

    private val postSnipers: HashSet<PostSniper> = HashSet()
    private val mutex: Mutex = Mutex()
    private var fixedRateTimer: Timer? = null


    fun startPolling() {
        stopPolling()
        fixedRateTimer = createFixedRateTimerForRefresh(refreshIntervalSeconds)
    }

    fun stopPolling() {
        fixedRateTimer?.cancel()
    }

    suspend fun register(postSniper: PostSniper) {
        mutex.withLock {
            postSnipers.add(postSniper)
        }
    }

    suspend fun deregister(postSniper: PostSniper) {
        mutex.withLock {
            postSnipers.remove(postSniper)
        }
    }

    /* creates a timer that executes generateNotificationTask once time is up */
    private fun createFixedRateTimerForRefresh(secondsInterval: Int): Timer {
        return (
                fixedRateTimer(
                    "RSS-Sniper-Refresh-Timer",
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

    suspend fun process() {
        mutex.withLock {
            for (postSniper in postSnipers) {
                postSniper.process()
            }
        }
    }
}