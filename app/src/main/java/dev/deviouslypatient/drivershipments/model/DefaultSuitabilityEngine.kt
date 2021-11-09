package dev.deviouslypatient.drivershipments.model

import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.lang.Exception

/*
The top-secret algorithm is:
If the length of the shipment's destination street name is even, the base suitability score (SS) is
    the number of vowels in the driver’s name multiplied by 1.5.
If the length of the shipment's destination street name is odd, the base SS is the number of
    consonants in the driver’s name multiplied by 1.
If the length of the shipment's destination street name shares any common factors (besides 1)
    with the length of the driver’s name, the SS is increased by 50% above the base SS.
 */

/*
Thoughts:
if you only have one shipment and one driver, that must be the best combination
if you only have one shipment and multiple drivers, the best combination is with the driver with the highest sus score
if you only have one driver and multiple shipments, the best combination is with the shipment with the highest sus score
once you have multiple drivers and multiple shipments, it has to become some kind of dynamic programming example
It's a 2D matrix of values, with every combination of driver and shipment
Then we have to figure out the best set of combinations
 */
class DefaultSuitabilityEngine: SuitabilityEngine {
    private val bestCombinations: ArrayList<Combination> = arrayListOf()

    override fun getDriverShipmentAssignments(
        driverNames: Array<String>,
        shipmentDestinations: Array<String>
    ): Single<ArrayList<Combination>> {
        return Single.create { emitter ->
            try {
                createAllCombinations(driverNames, shipmentDestinations)
                emitter.onSuccess(bestCombinations)
            } catch (e: Exception) {
                Timber.e(e, "Error calculating the most suitable combinations")
                emitter.onError(e)
            }

        }
    }

    fun createAllCombinations(driverNames: Array<String>, shipmentDestinations: Array<String>) {
        val drivers: ArrayList<Driver> = arrayListOf()
        val shipments: ArrayList<Shipment> = arrayListOf()

        // this would be an easy place to sanitize data, remove drivers or shipments that are empty strings
        driverNames.forEach{ drivers.add(Driver(it))}
        shipmentDestinations.forEach { shipments.add(Shipment(it)) }

        val combos: ArrayList<ArrayList<Combination>> = arrayListOf()
        drivers.forEach { driver ->
            val row: ArrayList<Combination> = arrayListOf()
            shipments.forEach { shipment ->
                row.add(createCombination(driver, shipment))
            }
            combos.add(row)
        }
        bestCombinations.clear()
        determineBestCombinations(combos)
    }

    fun determineBestCombinations(suitabilityScores: ArrayList<ArrayList<Combination>>) {
        //todo figure out something better than this really dumb approach
        if (suitabilityScores.size == 0) return
        if (suitabilityScores[0].size == 0) {
            // more drivers than shipments
            suitabilityScores.forEach { row ->
                bestCombinations.add(Combination(row[0].driver, null, 0.0))
            }
            return
        }

        val bestScore = suitabilityScores[0].maxOf { it.suitibilityScore }
        val index = suitabilityScores[0].indexOfFirst { it.suitibilityScore == bestScore }
        val bestCombination = suitabilityScores[0][index]
        bestCombinations.add(bestCombination)
        suitabilityScores.removeAt(0)
        suitabilityScores.forEach { col -> col.removeIf { it.shipment == bestCombination.shipment } }
        determineBestCombinations(suitabilityScores)
    }
    private fun createCombination(driver: Driver, shipment: Shipment): Combination {
        return Combination(driver, shipment, calculateSuitabilityScore(driver, shipment))
    }

    private fun calculateSuitabilityScore(driver: Driver, shipment: Shipment): Double {
        var baseSuitability = if (shipment.numberOfLetters % 2 == 0) {
            driver.numberOfVowels * 1.5
        } else {
            driver.numberOfConsonants * 1.0
        }
        if (sharesCommonFactors(shipment.numberOfLetters, driver.numberOfLetters)) {
            baseSuitability *= 1.5
        }
        return baseSuitability
    }

    private fun sharesCommonFactors(a: Int, b: Int): Boolean {
        return EvaluationFunctions.greatestCommonDivisor(a, b) > 1
    }

}
