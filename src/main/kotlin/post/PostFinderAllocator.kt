package post

import scraper.Scraper

interface PostFinderAllocator {
    fun getPostFinder(scraper: Scraper): PostFinder
}