package scraper

open class ScraperContext {
    /**
     * Max amount of posts to keep in memory to avoid duplicates
     *
     * If this amount is too low, you will get duplicate notifications about the same post
     * If this amount is too high, you will use excessive memory
     */
    open val rememberMaxPosts = 1000

    /**
     * This is meant to pass different user agent and request params to the scraper
     *
     * Consider randomizing or alternating props for each call to this method
     */
    open fun getRequestProps(): Map<String, String> {
        return mapOf(
            "User-Agent" to "9d42e38f-bebb-4a67-b45c-4968136bb534",
        )
    }

    open fun registerLoadedPostsForLog(scrapeReport: ScrapeReport) {}
}