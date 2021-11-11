package dev.deviouslypatient.drivershipments

import dev.deviouslypatient.drivershipments.model.EvaluationFunctions.greatestCommonDivisor
import dev.deviouslypatient.drivershipments.model.EvaluationFunctions.numberOfConsonants
import dev.deviouslypatient.drivershipments.model.EvaluationFunctions.numberOfVowels
import org.junit.Assert.assertEquals
import org.junit.Test

class EvaluationFunctionsTest {
    // for long running projects I would break these out to individual tests. I'm just saving
    // some overhead here by lumping together tests on the same function

    @Test
    fun testNumberOfVowels() {
        assertEquals(6, numberOfVowels("abcdefghijklmnopqrstuvwxyz"))
        assertEquals(6, numberOfVowels("ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
        assertEquals(6, numberOfVowels("aeiouy"))
        assertEquals(6, numberOfVowels("AEIOUY"))
        assertEquals(24, numberOfVowels("aaAA eeEE iiII ooOO uuUU yyYY"))
        assertEquals(0, numberOfVowels("qwrtpsdfghjklzxcvbnm"))
        assertEquals(0, numberOfVowels("QWRTPSDFGHJKLZXCVBNM"))
        assertEquals(0, numberOfVowels("qQ wW rR tT pP sS dD fF gG hH jJ kK lL zZ xX cC vV bB nN mM"))
        assertEquals(0, numberOfVowels(null))
        assertEquals(0, numberOfVowels(""))
        assertEquals(0, numberOfVowels("   "))
        assertEquals(0, numberOfVowels(" \t "))
        assertEquals(0, numberOfVowels("1234567890"))
        assertEquals(0, numberOfVowels("`~!@#$%^&*()-_=+[{]}\\|:;\"\',<.>/?"))
        assertEquals(5, numberOfVowels("Everardo Welch"))
        assertEquals(5, numberOfVowels("Orval Mayert"))
        assertEquals(5, numberOfVowels("Howard Emmerich"))
        assertEquals(6, numberOfVowels("Izaiah Lowe"))
        assertEquals(5, numberOfVowels("Monica Hermann"))
        assertEquals(4, numberOfVowels("Ellis Wisozk"))
        assertEquals(6, numberOfVowels("Noemie Murphy"))
        assertEquals(4, numberOfVowels("Cleve Durgan"))
        assertEquals(5, numberOfVowels("Murphy Mosciski"))
        assertEquals(5, numberOfVowels("Kaiser Sose"))
    }

    @Test
    fun testNumberOfConsonants() {
        assertEquals(20, numberOfConsonants("abcdefghijklmnopqrstuvwxyz"))
        assertEquals(20, numberOfConsonants("ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
        assertEquals(0, numberOfConsonants("aeiouy"))
        assertEquals(0, numberOfConsonants("AEIOUY"))
        assertEquals(0, numberOfConsonants("aaAA eeEE iiII ooOO uuUU yyYY"))
        assertEquals(20, numberOfConsonants("qwrtpsdfghjklzxcvbnm"))
        assertEquals(20, numberOfConsonants("QWRTPSDFGHJKLZXCVBNM"))
        assertEquals(40, numberOfConsonants("qQ wW rR tT pP sS dD fF gG hH jJ kK lL zZ xX cC vV bB nN mM"))
        assertEquals(0, numberOfConsonants(null))
        assertEquals(0, numberOfConsonants(""))
        assertEquals(0, numberOfConsonants("   "))
        assertEquals(0, numberOfConsonants(" \t "))
        assertEquals(0, numberOfConsonants("1234567890"))
        assertEquals(0, numberOfConsonants("`!@#$%^&*()_+:;\"\',<.>/?"))
        assertEquals(8, numberOfConsonants("Everardo Welch"))
        assertEquals(6, numberOfConsonants("Orval Mayert"))
        assertEquals(9, numberOfConsonants("Howard Emmerich"))
        assertEquals(4, numberOfConsonants("Izaiah Lowe"))
        assertEquals(8, numberOfConsonants("Monica Hermann"))
        assertEquals(7, numberOfConsonants("Ellis Wisozk"))
        assertEquals(6, numberOfConsonants("Noemie Murphy"))
        assertEquals(7, numberOfConsonants("Cleve Durgan"))
        assertEquals(9, numberOfConsonants("Murphy Mosciski"))
        assertEquals(5, numberOfConsonants("Kaiser Sose"))
    }

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
