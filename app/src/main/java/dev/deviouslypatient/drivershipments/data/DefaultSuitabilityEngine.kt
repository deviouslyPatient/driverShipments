package dev.deviouslypatient.drivershipments.data

import androidx.annotation.VisibleForTesting
import dev.deviouslypatient.drivershipments.model.Assignment
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/*
Thoughts:
I went through a lot of different approaches to this trying to consider what kind of data structures
would be the best for generating the permutations, whether it would be more performant to create a
data class for the drivers so that I would only have to compute the number of vowels and consonants
once, and whether or not a recursive function was the best idea. If I had more time I would have
liked to do more research and maybe taken some performance metrics on different approaches.

Potential issues with this approach:
If there are ever drivers with the same name, or shipments with the same destination then only one
will be considered
For very large lists of drivers and/or shipments this could lead to stack overflow issues.
 */

class DefaultSuitabilityEngine @Inject constructor(): SuitabilityEngine {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var bestAssignmentsScore: Double = 0.0
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var bestAssignments: List<Assignment> = listOf()
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var numberOfPermutationsConsidered = 0

    override fun calculateDriverShipmentAssignments(
        driverNames: List<String>,
        shipmentDestinations: List<String>
    ): List<Assignment> {
        val suitabilityMap = calculateSuitabilityMap(driverNames, shipmentDestinations)
        bestAssignments = emptyList()
        bestAssignmentsScore = 0.0
        numberOfPermutationsConsidered = 0
        // longer string lengths generally have more possible factors
        // longer names also generally have more vowels and consonants
        // put these first because they are likely to have higher suitability scores
        calculateBestPermutation(
            driverNames.sortedByDescending { it.length },
            shipmentDestinations.sortedByDescending { it.length },
            suitabilityMap,
            emptyList())
        Timber.d("Number of permutations considered: $numberOfPermutationsConsidered")
        Timber.d("Best permutation $bestAssignments  $bestAssignmentsScore")
        return bestAssignments
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun calculateSuitabilityMap(
        driverNames:List<String>,
        shipmentDestinations: List<String>
    ): MutableMap<Pair<String, String>, Double> {
        val suitabilityMap: MutableMap<Pair<String, String>, Double> = mutableMapOf()

        driverNames.forEach { driver ->
            shipmentDestinations.forEach {  shipment ->
                val key = Pair(driver, shipment)
                suitabilityMap[key] = calculateSuitabilityScore(driver, shipment)
            }
        }
        Timber.d("Map of suitability scores $suitabilityMap")
        return suitabilityMap
    }

    // this is a branching recursive function that determines which set of assignments results in
    // the highest possible overall suitability score. I'm not a huge fan of it updating
    // class level variables as it goes, but that saves putting those variables on the stack
    // I realized while writing tests that I didn't actually have to filter the suitability map
    // and therefore could have kept it as a class variable, I would love to test how that would
    // affect memory use and processing time on large data sets
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun calculateBestPermutation(
        remainingDrivers: List<String>,
        remainingShipments: List<String>,
        suitabilityMap: Map<Pair<String, String>, Double>,
        permutationSoFar: List<Assignment>
    ) {
        // no more options to consider -  we have a completed permutation
        if (suitabilityMap.isEmpty() || remainingDrivers.isEmpty() || remainingShipments.isEmpty()) {
            numberOfPermutationsConsidered++
            val permutationScore = permutationSoFar.sumOf { it.suitabilityScore }
            Timber.v("Current Permutation $permutationSoFar $permutationScore")

            if (permutationScore > bestAssignmentsScore) {
                // we have a new best permutation
                // make sure the permutation still includes all drivers
                if (remainingDrivers.isNotEmpty()) {
                    remainingDrivers.forEach {
                        permutationSoFar.plus(Assignment(it, null, 0.0))
                    }
                }
                bestAssignments = permutationSoFar
                bestAssignmentsScore = permutationScore
            }
            return
        }

        // heuristic test to see if we should keep considering permutations down this branch
        if (bestAssignmentsScore >
            maxPossibleScoreForPermutation(remainingDrivers, remainingShipments, suitabilityMap, permutationSoFar)){
            return
        }

        // consider each next possible assignment in the current permutation
        if (remainingDrivers.size > remainingShipments.size) {
            // if there are more drivers than shipments then every shipment should get an assignment
            // create branched permutations for if the next shipment were assigned to each driver
            val selectedShipment = remainingShipments[0]
            remainingDrivers.forEach { driver ->
                val score = suitabilityMap[Pair(driver, selectedShipment)] ?: 0.0
                calculateBestPermutation(
                    remainingDrivers.filter { it != driver },
                    remainingShipments.filter { it != selectedShipment },
                    suitabilityMap.filter { it.key.first != driver && it.key.second != selectedShipment },
                    permutationSoFar.plus(Assignment(driver, selectedShipment, score))
                )
            }
        } else {
            // if there are at least as many shipments as drivers every driver gets an assignment
            // create branched permutations for if the next driver were assigned each shipment
            val selectedDriver = remainingDrivers[0]
            remainingShipments.forEach { shipment ->
                val score = suitabilityMap[Pair(selectedDriver, shipment)] ?: 0.0
                calculateBestPermutation(
                    remainingDrivers.filter { it != selectedDriver },
                    remainingShipments.filter { it != shipment },
                    suitabilityMap.filter { it.key.first != selectedDriver && it.key.second != shipment },
                    permutationSoFar.plus(Assignment(selectedDriver, shipment, score))
                )
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun maxPossibleScoreForPermutation(
        remainingDrivers: List<String>,
        remainingShipments: List<String>,
        suitabilityMap: Map<Pair<String, String>, Double>,
        permutationSoFar: List<Assignment>
    ): Double {
        var superMax = permutationSoFar.sumOf { it.suitabilityScore }

        if (suitabilityMap.isEmpty() || remainingDrivers.isEmpty() || remainingShipments.isEmpty()) {
            return superMax
        }

        if (remainingDrivers.size > remainingShipments.size) {
            // add the highest score each shipment could ever possibly contribute
            remainingShipments.forEach { shipment ->
                superMax += Collections.max(suitabilityMap.filter { it.key.second == shipment }.values)
            }
        } else {
            // add the highest score each driver could ever possibly contribute
            remainingDrivers.forEach { driver ->
                superMax += Collections.max(suitabilityMap.filter { it.key.first == driver }.values)
            }
        }
        return superMax
    }

    /*
    The top-secret algorithm is:
    If the length of the shipment's destination street name is even, the base suitability score (SS)
        is the number of vowels in the driver’s name multiplied by 1.5.
    If the length of the shipment's destination street name is odd, the base SS is the number of
        consonants in the driver’s name multiplied by 1.
    If the length of the shipment's destination street name shares any common factors (besides 1)
        with the length of the driver’s name, the SS is increased by 50% above the base SS.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun calculateSuitabilityScore(driver: String, shipment: String): Double {
        val driverVowels = EvaluationFunctions.numberOfVowels(driver)
        val driverConsonants = EvaluationFunctions.numberOfConsonants(driver)

        var score = if (shipment.length %2 == 0) {
            driverVowels * 1.5
        } else {
            driverConsonants * 1.0
        }
        if (EvaluationFunctions.greatestCommonDivisor(shipment.length, driver.length) > 1 ) {
            score *= 1.5
        }
        return score
    }
}
