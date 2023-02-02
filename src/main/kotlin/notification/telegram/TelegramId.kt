package notification.telegram

import notification.NotifierCredentials

class TelegramId(override val value: String) : NotifierCredentials {
    constructor(value: Long) : this(value.toString())
}