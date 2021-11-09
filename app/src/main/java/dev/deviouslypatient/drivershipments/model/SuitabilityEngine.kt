package dev.deviouslypatient.drivershipments.model

import timber.log.Timber

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
class SuitabilityEngine {
    fun createAllCombinations(driverNames: List<String>, shipmentDestinations: List<String>) {
        val drivers: ArrayList<Driver> = arrayListOf()
        val shipments: ArrayList<Shipment> = arrayListOf()
        driverNames.forEach{ drivers.add(Driver(it))}
        shipmentDestinations.forEach { shipments.add(Shipment(it)) }

        val rows = drivers.size
        val cols = shipments.size
//        val allCombinations = Array(rows) { r -> Array(cols) { c -> calculateSuitability(drivers[r], shipments[c]) } }
        val allCombinations = Array(rows) { r -> Array(cols) { c -> calculateSuitabilityScore(drivers[r], shipments[c]) } }

        for(row in allCombinations) {
            Timber.d(row.contentToString())
        }

    }

    fun calculateSuitabilityScore(driver: Driver, shipment: Shipment): Double {
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

    fun calculateSuitability(driver: Driver, shipment: Shipment): Combination {
        return Combination(driver, shipment, calculateSuitabilityScore(driver, shipment))
    }

    private fun sharesCommonFactors(a: Int, b: Int): Boolean {
        return EvaluationFunctions.greatestCommonDivisor(a, b) > 1
    }

}
