package utils


class MaxSizeHashSet<T>(private val maxEntries: Int) : LinkedHashSet<T>() {
    override fun add(element: T): Boolean {
        val added = super.add(element)
        if (size > maxEntries) {
            remove(iterator().next())
        }
        return added
    }

    fun createTrimmedList(oldList: Collection<T>): List<T> {
        val newList = ArrayList<T>()
        for (element in oldList) {
            if (!this.contains(element)) {
                this.add(element)
                newList.add(element)
            }
        }
        return newList
    }
}
