package scraper

import java.util.*

data class ScrapeReport(
    val scraperName: String,
    val date: Date,
    val postsRetrieved: Int,
    val lastPostTitle: String?
)
