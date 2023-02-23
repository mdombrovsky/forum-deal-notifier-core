package scraper

import java.io.InputStream
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime

fun URL.getInputSteam(userAgent: String = "9d42e38f-bebb-4a67-b45c-4968136bb534"): InputStream {
    return this.openConnection().apply {
        setRequestProperty(
            "User-Agent",
            userAgent
        )
        connectTimeout = 5000
        readTimeout = 5000
    }.inputStream
}

fun getData(urlString: String): String {
    val url = URL(urlString)

    return url.getData()
}

fun URL.getData(): String {
    val response: String = try {
        this.getInputSteam().bufferedReader().readText()
    } catch (e: Exception) {
        println("URL: ${this.host}, Error getting response: $e")
        ""
    }
    return response
}

fun getTimeZoneOffset(): String {
    val zone = ZoneId.of(ZoneId.systemDefault().toString())
    return ZonedDateTime.now(zone).offset.toString()
        .replace(":", "")
}