package user

import notification.NotifierUserCredentials
import query.QueriesManager

class User(val name: String) {
    private val credentials = HashMap<String, NotifierUserCredentials>()
    val queriesManager = QueriesManager()

    fun registerCredential(id: NotifierUserCredentials) {
        val key = id::class.java.canonicalName
        if (credentials.containsKey(key)) {
            throw UnsupportedOperationException("NotifierUserCredentials already exists")
        }
        credentials[key] = id
    }

    fun deregisterCredential(clazz: Class<out NotifierUserCredentials>): Boolean {
        val key = clazz.canonicalName
        if (!credentials.containsKey(key)) {
            return false
        }
        return credentials.remove(key) != null
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : NotifierUserCredentials> retrieveCredential(clazz: Class<T>): T? {
        val key = clazz.canonicalName
        if (credentials[key] == null) {
            return null
        }
        return (credentials[key] as? T
            ?: throw UnsupportedOperationException("NotifierUserCredentials retrieval fatal error"))
    }
}
