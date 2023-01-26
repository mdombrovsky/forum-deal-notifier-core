package scraper

import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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

fun getTimeZoneOffset(): String {
    val zone = ZoneId.of(ZoneId.systemDefault().toString())
    val zoneAbbreviationFormatter = DateTimeFormatter.ofPattern("z", Locale.ENGLISH)
    return /*ZonedDateTime.now(zone).format(zoneAbbreviationFormatter) +*/ ZonedDateTime.now(zone).offset.toString()
        .replace(":", "")
}