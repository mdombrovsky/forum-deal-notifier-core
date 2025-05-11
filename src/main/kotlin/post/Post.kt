import utils.getTimeDifference
import java.io.Serializable
import java.util.*

class Post(
    val title: String = "",
    val url: String,
    val source: String = "",
    val date: Date? = null
) : Serializable {


    private val stringToSearchNoSpacesLowercase: String =
        (title.replace("\\s+".toRegex(), "")).toLowerCase()

    fun searchNoSpacesLowerCase(searchString: String): Boolean {
        return stringToSearchNoSpacesLowercase.contains(searchString)
    }

    override fun toString(): String {
        return "Title: {$title}, Date: {${date}}, Source {${source}}, URL: {${url}}\n"
    }

    fun getAge(): String {
        return if (date != null) {
            val diff = System.currentTimeMillis() - date.time
            getTimeDifference(diff) + " ago"
        } else {
            "Null date"
        }

    }

    fun toPrettyString(): String {
        return "$title\n${url}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }
}