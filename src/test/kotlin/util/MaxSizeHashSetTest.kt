package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.MaxSizeHashSet

class MaxSizeHashSetTest {

    @Test
    fun combinedTest() {
        val maxSizeHashSet = MaxSizeHashSet<Int>(5)
        maxSizeHashSet.addAll(listOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertEquals(maxSizeHashSet.contains(1), false)
        assertEquals(maxSizeHashSet.contains(2), false)
        assertEquals(maxSizeHashSet.contains(3), false)
        assertEquals(maxSizeHashSet.contains(4), true)
        assertEquals(maxSizeHashSet.contains(5), true)
        assertEquals(maxSizeHashSet.contains(6), true)
        assertEquals(maxSizeHashSet.contains(7), true)
        assertEquals(maxSizeHashSet.contains(8), true)

        maxSizeHashSet.add(9)
        assertEquals(maxSizeHashSet.contains(4), false)
        assertEquals(maxSizeHashSet.contains(9), true)
    }
}