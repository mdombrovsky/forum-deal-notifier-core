package user

import notification.NotifierCredentials
import query.QueriesManager

class User(val name: String) {
    private val credentials = HashMap<String, NotifierCredentials>()
    val queriesManager = QueriesManager()

    fun registerCredential(id: NotifierCredentials) {
        val key = id::class.java.canonicalName
        if (credentials.containsKey(key)) {
            throw UnsupportedOperationException("NotifierCredentials already exists")
        }
        credentials[key] = id
    }

    fun deregisterCredential(clazz: Class<out NotifierCredentials>): Boolean {
        val key = clazz.canonicalName
        if (!credentials.containsKey(key)) {
            return false
        }
        return credentials.remove(key) != null
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : NotifierCredentials> retrieveCredential(clazz: Class<T>): T? {
        val key = clazz.canonicalName
        if (credentials[key] == null) {
            return null
        }
        return (credentials[key] as? T
            ?: throw UnsupportedOperationException("NotifierCredentials retrieval fatal error"))
    }
}
