package dev.deviouslypatient.drivershipments

import dev.deviouslypatient.drivershipments.model.Assignment
import dev.deviouslypatient.drivershipments.model.DefaultSuitabilityEngine
import org.junit.Assert.assertEquals
import org.junit.Test

private const val DELTA = 0.0001 // how close doubles should be in equals comparisons
class DefaultSuitabilityEngineTest {

    @Test
    fun testCalculateSuitabilityScore() {
        val engine = DefaultSuitabilityEngine()
        // even with factors > 1
        assertEquals(6.75, engine.calculateSuitabilityScore("aaa", "123456"), DELTA)
        assertEquals(0.0, engine.calculateSuitabilityScore("bbb", "123456"), DELTA)

        // even without factors > 1
        assertEquals(4.5, engine.calculateSuitabilityScore("aaa", "1234"), DELTA)
        assertEquals(0.0, engine.calculateSuitabilityScore("bbb", "1234"), DELTA)

        // odd with factors > 1
        assertEquals(0.0, engine.calculateSuitabilityScore("aaa", "123"), DELTA)
        assertEquals(4.5, engine.calculateSuitabilityScore("bbb", "123"), DELTA)

        // odd without factors > 1
        assertEquals(0.0, engine.calculateSuitabilityScore("aaa", "12345"), DELTA)
        assertEquals(3.0, engine.calculateSuitabilityScore("bbb", "12345"), DELTA)

        assertEquals(11.25, engine.calculateSuitabilityScore("Everardo Welch", "63187 Volkman Garden Suite 447"), DELTA)
        assertEquals(9.0, engine.calculateSuitabilityScore("Orval Mayert", "9856 Marvin Stravenue"), DELTA)
        assertEquals(5.0, engine.calculateSuitabilityScore("Kaiser Sose", "987 Champlin Lake"), DELTA)
    }

    @Test
    fun testCalculateSuitabilityMap() {
        val drivers: List<String> = listOf("aaa", "bbb")
        val shipments: List<String> = listOf("123", "1234", "12345")
        val engine = DefaultSuitabilityEngine()

        val map = engine.calculateSuitabilityMap(drivers, shipments)
        assertEquals(6, map.entries.size)
        assert(map.containsKey(Pair("aaa", "123")))
        assert(map.containsKey(Pair("aaa", "1234")))
        assert(map.containsKey(Pair("aaa", "12345")))
        assert(map.containsKey(Pair("bbb", "123")))
        assert(map.containsKey(Pair("bbb", "1234")))
        assert(map.containsKey(Pair("bbb", "12345")))
    }

    @Test
    fun testMaxPossibleScoreForPermutationWhenMoreShipmentsThanDrivers() {
        val drivers: List<String> = listOf("aaa", "bbb")
        val shipments: List<String> = listOf("123", "1234", "12345")
        val engine = DefaultSuitabilityEngine()
        val map = mutableMapOf(
            Pair("aaa","123") to 100.0,
            Pair("aaa","1234") to 1.0,
            Pair("aaa", "12345") to 2.0,
            Pair("bbb", "123") to 200.0,
            Pair("bbb", "1234") to 3.0,
            Pair("bbb", "12345") to 4.0
        )
        val permutationSoFar = listOf(Assignment("ccc","123456", 25.5))

        var result = engine.maxPossibleScoreForPermutation(drivers, shipments, map, listOf())
        assertEquals(300.0, result, DELTA)

        result = engine.maxPossibleScoreForPermutation(drivers, shipments, map, permutationSoFar)
        assertEquals(325.5, result, DELTA)

        result = engine.maxPossibleScoreForPermutation(emptyList(), shipments, map, permutationSoFar)
        assertEquals(25.5, result, DELTA)

        result = engine.maxPossibleScoreForPermutation(drivers, emptyList(), map, permutationSoFar)
        assertEquals(25.5, result, DELTA)

        result = engine.maxPossibleScoreForPermutation(drivers, shipments, emptyMap(), permutationSoFar)
        assertEquals(25.5, result, DELTA)
    }

    @Test
    fun testMaxPossibleScoreForPermutationWhenMoreDriversShipments() {
        val drivers: List<String> = listOf("aaa", "bbb", "ddd")
        val shipments: List<String> = listOf("123", "1234")
        val engine = DefaultSuitabilityEngine()
        val map = mutableMapOf(
            Pair("aaa","123") to 100.0,
            Pair("aaa","1234") to 200.0,
            Pair("bbb", "123") to 1.0,
            Pair("bbb", "1234") to 2.0,
            Pair("ddd", "123") to 3.0,
            Pair("ddd", "1234") to 4.0
        )
        val permutationSoFar = listOf(Assignment("ccc","123456", 25.5))

        var result = engine.maxPossibleScoreForPermutation(drivers, shipments, map, listOf())
        assertEquals(300.0, result, DELTA)

        result = engine.maxPossibleScoreForPermutation(drivers, shipments, map, permutationSoFar)
        assertEquals(325.5, result, DELTA)

        result = engine.maxPossibleScoreForPermutation(emptyList(), shipments, map, permutationSoFar)
        assertEquals(25.5, result, DELTA)

        result = engine.maxPossibleScoreForPermutation(drivers, emptyList(), map, permutationSoFar)
        assertEquals(25.5, result, DELTA)

        result = engine.maxPossibleScoreForPermutation(drivers, shipments, emptyMap(), permutationSoFar)
        assertEquals(25.5, result, DELTA)
    }

    @Test
    //todo add tests for different data sets to improve confidence in the suitability engine
    fun testCalculateBestPermutation() {
        val drivers: List<String> = listOf("aaa", "bbb")
        val shipments: List<String> = listOf("123", "1234", "12345")
        val engine = DefaultSuitabilityEngine()
        val map = mutableMapOf(
            Pair("aaa","123") to 100.0,
            Pair("aaa","1234") to 1.0,
            Pair("aaa", "12345") to 2.0,
            Pair("bbb", "123") to 200.0,
            Pair("bbb", "1234") to 3.0,
            Pair("bbb", "12345") to 4.0
        )

        engine.calculateBestPermutation(drivers, shipments, map, emptyList())

        val assignments = engine.bestAssignments
        val score = engine.bestAssignmentsScore
        assertEquals(202.0, score, DELTA)
        assertEquals(score, assignments.sumOf { it.suitabilityScore }, DELTA)
        assertEquals(2, assignments.size)
        assert(assignments.contains(Assignment("aaa", "12345", 2.0)))
        assert(assignments.contains(Assignment("bbb", "123", 200.0)))
    }
}