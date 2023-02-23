package utils

fun getTimeDifference(difference: Long): String {
    var seconds: Long = (difference / 1000)
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
        }

        hours != zero -> "$hours hour" + if (hours != one) {
            "s"
        } else {
            ""
        }

        minutes != zero -> "$minutes minute" + if (minutes != one) {
            "s"
        } else {
            ""
        }

        else -> "$seconds second" + if (seconds != one) {
            "s"
        } else {
            ""
        }
    }
}