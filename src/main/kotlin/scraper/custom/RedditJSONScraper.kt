package scraper.custom

import Post
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import post.SortedPostList
import scraper.ScrapeReport
import scraper.Scraper
import scraper.ScraperContext
import java.util.*

class RedditJSONScraper(private val subReddit: String, private val scraperContext: ScraperContext = ScraperContext()) :
    Scraper(scraperContext) {

    init {
        if (!subReddit.isValidSubredditName()) {
            throw IllegalArgumentException("Invalid subreddit name: $subReddit")
        }
    }

    override suspend fun getAllPosts(): SortedPostList {
        return redditJSONToPosts(
            getData(
                "https://www.reddit.com/r/${subReddit}/new.json?limit=100"
            )
        ).apply {
            println("Time: ${Date()}, ${getName()}, Retrieved ${this.size} posts, last post: ${this.getOrNull(0)?.title}")
            scraperContext.registerLoadedPostsForLog(
                ScrapeReport(
                    getName(),
                    Date(),
                    this.size,
                    this.getOrNull(0)?.title
                )
            )
        }
    }


    override fun getName(): String {
        return "Reddit, subreddit = $subReddit"
    }

    /**
     * Converts reddit json into posts
     *
     * @param jsonString The reddit json
     */
    private fun redditJSONToPosts(jsonString: String): SortedPostList {
        val posts = SortedPostList()

        if (jsonString != "") {
            val json = JSONObject(jsonString)

            if (json.getString("kind") != "Listing") {
                return posts
            }

            val jsonPostArray: JSONArray = json.getJSONObject("data").getJSONArray("children")

            for (i in 0 until jsonPostArray.length()) {
                val jsonPost: JSONObject = jsonPostArray.getJSONObject(i)
                val post: Post = createRedditPost(jsonPost)


                posts.add(post)
            }

        }
        return posts

    }


    private fun createRedditPost(jsonPost: JSONObject): Post {
        if (jsonPost.getString("kind") == "t3") {
            val jsonPostData: JSONObject = jsonPost.getJSONObject("data")

            val id = jsonPostData.getString("id")
            return (Post(
                title = jsonPostData.getString("title"),
                url = "https://www.reddit.com/comments/$id",
                source = "Reddit: r/$subReddit",
                date = Date(jsonPostData.getLong("created_utc") * 1000)
            ))

        } else {
            throw JSONException("Incorrect Format for Reddit Post")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RedditJSONScraper

        if (subReddit != other.subReddit) return false

        return true
    }

    override fun hashCode(): Int {
        return subReddit.hashCode()
    }

    override fun getConfig(): String {
        return subReddit
    }

    /**
     * Checks if a string is a valid subreddit name according to:
     * https://stackoverflow.com/questions/21109968/python-regex-to-match-subreddit-names
     */
    private fun String.isValidSubredditName(): Boolean =
        this.matches(Regex("([a-zA-Z0-9][_a-zA-Z0-9]{2,20})"))
}