package notification

class LongNotifierUserCredentials(override val value: String) : NotifierUserCredentials {
    constructor(value: Long) : this(value.toString())
}