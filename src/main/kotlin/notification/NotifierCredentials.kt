package notification

interface NotifierCredentials {
    /**
     * This is meant to store a key that will allow a notifier to identify
     * the user to whom to send a notification to
     */
    val value: String
}