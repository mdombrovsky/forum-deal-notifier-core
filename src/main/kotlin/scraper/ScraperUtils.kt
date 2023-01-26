package scraper

import java.net.URL

fun getData(urlString: String, userAgent: String = "9d42e38f-bebb-4a67-b45c-4968136bb534"): String {
    val url = URL(urlString)
    val response: String

    response = try {
        url.openConnection().apply {
            setRequestProperty(
                "User-Agent",
                userAgent
            )
        }.inputStream.bufferedReader().readText()
    } catch (e: Exception) {
        println("URL: ${urlString}, Error getting response: $e")
        ""
    }
    return response
}
