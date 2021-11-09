package dev.deviouslypatient.drivershipments

// test class to look into using a Map with a Pair key to find best permutation of assignments
// this works quickly to find the best permutation when the number of permutations is low,
// but the example will end up with like 3 million possible permutations
// we need to figure out some kind a heuristic or suggest that this computation should instead
// be done on a much beefier machine than an android device, and the results sent to the app.
fun main() {
    val drivers: MutableList<String> = mutableListOf("Mary", "Amanda", "Jensen")
    val shipments: MutableList<String> = mutableListOf("LA", "Portland", "Dallas", "NYC")

    val combinations = mapOf<Pair<String, String>, Double>(
        Pair("Mary", "Portland") to 10.0,
        Pair("Mary", "Dallas") to 2.0,
        Pair("Mary", "LA") to 1.0,
        Pair("Mary", "NYC") to 6.0,
        Pair("Amanda", "Portland") to 4.0,
        Pair("Amanda", "Dallas") to 8.0,
        Pair("Amanda", "LA") to 2.0,
        Pair("Amanda", "NYC") to 5.0,
        Pair("Jensen", "Portland") to 3.0, 
        Pair("Jensen", "Dallas") to 10.0,
        Pair("Jensen", "LA") to 7.0,
        Pair("Jensen", "NYC") to 17.0,
        Pair("Rick", "LA") to 17.0,
        Pair("Rick", "Dallas") to 2.0,
        Pair("Jensen", "Portland") to 3.0
    )
    val copy = combinations.filter { it.key.first != drivers[0] && it.key.second != shipments[0]}
    println(combinations)
    println("---")
    permutation(drivers, shipments, combinations, arrayListOf())
    println("Best $bestCombo  $bestComboScore" )
}

var bestCombo:List<Holder> = emptyList()
var bestComboScore: Double = 0.0
private fun permutation(
    drivers: List<String>,
    shipments: List<String>,
    combos: Map<Pair<String, String>, Double>,
    permutationSoFar: List<Holder>
) {
    if (combos.isEmpty() || drivers.isEmpty() || shipments.isEmpty()) {
        // we have a permutation
        val permutationScore = permutationSoFar.sumOf { it.score }
        if ( permutationScore> bestComboScore ) {
            bestCombo = permutationSoFar
            bestComboScore = permutationScore
        }
        println("Current $permutationSoFar   $permutationScore")

        return
    }
    if (drivers.size > shipments.size) {
        val selectedShipment = shipments[0]
        drivers.forEach { driver ->
            val score = combos[Pair(driver, selectedShipment)] ?: 0.0
            permutation(
                drivers.filter { it != driver },
                shipments.filter { it != selectedShipment },
                combos.filter { it.key.first != driver && it.key.second != selectedShipment },
                permutationSoFar.plus(Holder(driver, selectedShipment, score))
            )
        }
    }
    else {
        val selectedDriver = drivers[0]
        shipments.forEach { shipment ->
            val score = combos[Pair(selectedDriver, shipment)] ?: 0.0
            permutation(
                drivers.filter { it != selectedDriver },
                shipments.filter { it != shipment },
                combos.filter { it.key.first != selectedDriver && it.key.second != shipment },
                permutationSoFar.plus(Holder(selectedDriver, shipment, score))
            )
        }
    }
}

data class Holder(val driver: String, val shipment: String, val score:Double)
