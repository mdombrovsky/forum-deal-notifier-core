package scraper.custom

import Post
import SortedPostList
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import scraper.Scraper
import java.net.URL
import java.util.*

class RedditJSONScraper(private val subReddit: String) : Scraper() {

    override suspend fun getAllPosts(): SortedPostList {
        return redditJSONToPosts(
            getData(
                "https://www.reddit.com/r/${subReddit}/new.json?limit=100"
            )
        )
    }

    private fun getData(urlString: String): String {
        val url = URL(urlString)
        val response: String

        response = try {
            url.openConnection().apply {
                setRequestProperty(
                    "User-Agent",
                    "9d42e38f-bebb-4a67-b45c-4968136bb534"
                )
            }.inputStream.bufferedReader().readText()
        } catch (e: Exception) {
            println(this.javaClass.simpleName + ": Error getting response: $e")
            ""
        }
        return response
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
                url = "https://www.reddit.com/$id",
                date = Date(jsonPostData.getLong("created_utc") * 1000),
                source = "Reddit: r/$subReddit"
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
}