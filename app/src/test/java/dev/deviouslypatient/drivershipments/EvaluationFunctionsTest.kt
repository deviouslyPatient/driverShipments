package dev.deviouslypatient.drivershipments

import dev.deviouslypatient.drivershipments.model.EvaluationFunctions.greatestCommonDivisor
import org.junit.Assert.assertEquals
import org.junit.Test

class EvaluationFunctionsTest {
    @Test
    fun testGreatestCommonDivisor() {
        // I hate that zero is so weird here
        assertEquals(0, greatestCommonDivisor(0, 0))
        assertEquals(30, greatestCommonDivisor(0, 30))
        assertEquals(30, greatestCommonDivisor(30, 0))
        assertEquals(1, greatestCommonDivisor(1, 0))
        assertEquals(1, greatestCommonDivisor(0, 1))
        assertEquals(1, greatestCommonDivisor(1, 30))
        assertEquals(1, greatestCommonDivisor(30, 1))
        assertEquals(1, greatestCommonDivisor(2, 3))
        assertEquals(1, greatestCommonDivisor(3, 2))
        assertEquals(2, greatestCommonDivisor(2, 8))
        assertEquals(2, greatestCommonDivisor(8, 2))
        assertEquals(3, greatestCommonDivisor(9, 6))
        assertEquals(3, greatestCommonDivisor(3, 9))
        assertEquals(9, greatestCommonDivisor(9, 9))
        assertEquals(6, greatestCommonDivisor(12, 18))
        assertEquals(1, greatestCommonDivisor(13, 23))
        assertEquals(6, greatestCommonDivisor(12, -18))
        assertEquals(6, greatestCommonDivisor(-12, -18))
        assertEquals(6, greatestCommonDivisor(-12, 18))
        assertEquals(13, greatestCommonDivisor(39, 91))
    }
}
