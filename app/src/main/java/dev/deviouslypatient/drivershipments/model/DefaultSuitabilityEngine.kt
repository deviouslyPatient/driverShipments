package dev.deviouslypatient.drivershipments.model

import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.lang.Exception

/*
Thoughts:
I went through a lot of different approaches to this trying to consider what kind of data structures
would be the best for generating the permutations, whether it would be more performant to create a
data class for the drivers so that I would only have to compute the number of vowels and consonants
once, and whether or not a recursive function was the best idea. If I had more time I would have
liked to do more research and maybe taken some performance metrics on different approaches.

Potential issues with this approach:
if there are ever drivers with the same name, or shipments with the same destination then only one
will be considered
 */
class DefaultSuitabilityEngine: SuitabilityEngine {
     var bestAssignmentsScore: Double = 0.0
     var bestAssignments: List<Assignment> = listOf()

    override fun getDriverShipmentAssignments(
        driverNames: List<String>,
        shipmentDestinations: List<String>
    ): Single<List<Assignment>> {
        return Single.create { emitter ->
            try {
                calculateSuitabilityMap(driverNames, shipmentDestinations)
                emitter.onSuccess(bestAssignments)
            } catch (e: Exception) {
                Timber.e(e, "Error calculating the most suitable shipment assignments")
                emitter.onError(e)
            }
        }
    }

    fun calculateSuitabilityMap(driverNames:List<String>, shipmentDestinations: List<String>) {
        val suitabilityMap: MutableMap<Pair<String, String>, Double> = mutableMapOf()

        driverNames.forEach { driver ->
            shipmentDestinations.forEach {  shipment ->
                val key = Pair(driver, shipment)
                suitabilityMap[key] = calculateSuitabilityScore(driver, shipment)
            }
        }
        Timber.d("Map of suitability scores $suitabilityMap")
        bestAssignments = emptyList()
        bestAssignmentsScore = 0.0
        comparePermutations(driverNames, shipmentDestinations, suitabilityMap, emptyList())
        Timber.d("Best permutation $bestAssignments  $bestAssignmentsScore")
    }

    private fun comparePermutations(
        drivers: List<String>,
        shipments: List<String>,
        suitabilityMap: Map<Pair<String, String>, Double>,
        permutationSoFar: List<Assignment>
    ) {
        // no more options to consider -  we have a permutation
        if (suitabilityMap.isEmpty() || drivers.isEmpty() || shipments.isEmpty()) {
            if (drivers.isNotEmpty()) {
                // add any drivers who may not have been assigned a shipment
                drivers.forEach {
                    permutationSoFar.plus(Assignment(it, null, 0.0))
                }
            }
            val permutationScore = permutationSoFar.sumOf { it.suitibilityScore }
            if (permutationScore > bestAssignmentsScore) {
                bestAssignments = permutationSoFar
                bestAssignmentsScore = permutationScore
            }
            Timber.d("Current Permutation $permutationSoFar $permutationScore")
            return
        }

        // keep building up the assignments in the current permutation of assignments
        if (drivers.size > shipments.size) {
            val selectedShipment = shipments[0]
            drivers.forEach { driver ->
                val score = suitabilityMap[Pair(driver, selectedShipment)] ?: 0.0
                comparePermutations(
                    drivers.filter { it != driver },
                    shipments.filter { it != selectedShipment },
                    suitabilityMap.filter { it.key.first != driver && it.key.second != selectedShipment },
                    permutationSoFar.plus(Assignment(driver, selectedShipment, score))
                )
            }
        } else {
            val selectedDriver = drivers[0]
            shipments.forEach { shipment ->
                val score = suitabilityMap[Pair(selectedDriver, shipment)] ?: 0.0
                comparePermutations(
                    drivers.filter { it != selectedDriver },
                    shipments.filter { it != shipment },
                    suitabilityMap.filter { it.key.first != selectedDriver && it.key.second != shipment },
                    permutationSoFar.plus(Assignment(selectedDriver, shipment, score))
                )
            }
        }
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
    private fun calculateSuitabilityScore(driver: String, shipment: String): Double {
        val driverVowels = EvaluationFunctions.numberOfVowels(driver)
        val driverConsonants = EvaluationFunctions.numberOfConsonants(driver)

        var baseSuitability = if (shipment.length %2 == 0) {
            driverVowels * 1.5
        } else {
            driverConsonants * 1.0
        }
        if (EvaluationFunctions.greatestCommonDivisor(shipment.length, driver.length) > 1 ) {
            baseSuitability *= 1.5
        }
        return baseSuitability
    }
}
