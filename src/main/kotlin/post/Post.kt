import java.io.Serializable
import java.util.*

class Post(
    val title: String = "",
    val url: String = "",
    val date: Date,
    val source: String = ""
) : Serializable {

    val id: String = title + date.time

    private val stringToSearchNoSpacesLowercase: String =
        (title.replace("\\s+".toRegex(), "")).toLowerCase()

    fun searchNoSpacesLowerCase(searchString: String): Boolean {
        return stringToSearchNoSpacesLowercase.contains(searchString)
    }

    override fun toString(): String {
        return "Title: {$title}, Date: {${date}}, Source {${source}}, URL: {${url}}\n"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun getAge(): String {
        val diff = System.currentTimeMillis() - date.time
        val curr = Date(System.currentTimeMillis())
        var seconds: Long = (diff / 1000)
        var minutes: Long = seconds / 60
        var hours: Long = minutes / 60
        val days: Long = hours / 24
        val zero: Long = 0
        val one: Long = 1
        seconds %= 60
        minutes %= 60
        hours %= 24

        return when {
            days != zero -> "$days day" + if (days != one) {
                "s"
            } else {
                ""
            } + "ago"

            hours != zero -> "$hours hour" + if (hours != one) {
                "s"
            } else {
                ""
            } + " ago "

            minutes != zero -> "$minutes minute" + if (minutes != one) {
                "s"
            } else {
                ""
            } + " ago"

            else -> "$seconds second" + if (seconds != one) {
                "s"
            } else {
                ""
            } + " ago"
        }

    }

    fun toPrettyString(): String {
        return "{$title}\n${url}"
    }

}