package post

import scraper.Scraper

interface PostFinderAllocator {
    fun getPostFinder(scraper: Scraper, runBlocking: Boolean = false): PostFinder
}