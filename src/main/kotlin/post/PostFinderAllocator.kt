package post

import scraper.Scraper

interface PostFinderAllocator {
    /**
     * This will get a PostFinder for a Scraper, instead of directly using the Scraper
     * in order to combine multiple network calls into one
     *
     * @param scraper the target scraper
     * @param runBlocking use this when debugging/demoing, this will bypass concurrency mutex
     *
     * @return the PostFinder for the passed in scraper
     */
    fun getPostFinder(scraper: Scraper, runBlocking: Boolean = false): PostFinder
}