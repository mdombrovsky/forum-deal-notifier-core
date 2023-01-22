package user

import notification.NotifierIdentification

class User(val name: String) {
    private val ids = HashMap<String, NotifierIdentification>()

    fun registerId(id: NotifierIdentification) {
        val key = id::class.java.canonicalName
        if (ids.containsKey(key)) {
            throw UnsupportedOperationException("NotifierIdentification already exists")
        }
        ids[key] = id
    }

    fun deregisterId(clazz: Class<out NotifierIdentification>) {
        val key = clazz.canonicalName
        if (!ids.containsKey(key)) {
            throw UnsupportedOperationException("NotifierIdentification does not exist")
        }
        ids.remove(key)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : NotifierIdentification> retrieveId(clazz: Class<T>): T {
        val key = clazz.canonicalName
        if (ids[key] == null) {
            throw UnsupportedOperationException("NotifierIdentification does not exist")
        }
        return (ids[key] as? T ?: throw UnsupportedOperationException("NotifierIdentification retrieval fatal error"))
    }
}
