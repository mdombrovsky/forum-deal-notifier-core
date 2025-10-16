package scraper

import java.time.ZoneId
import java.time.ZonedDateTime

fun getTimeZoneOffset(): String {
    val zone = ZoneId.of(ZoneId.systemDefault().toString())
    return ZonedDateTime.now(zone).offset.toString()
        .replace(":", "")
}
