package post

import scraper.Scraper

interface PostFinderAllocator {
    suspend fun getPostFinder(scraper: Scraper): PostFinder
}