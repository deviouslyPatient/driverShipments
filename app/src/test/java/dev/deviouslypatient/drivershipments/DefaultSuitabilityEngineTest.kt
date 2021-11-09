package dev.deviouslypatient.drivershipments

import dev.deviouslypatient.drivershipments.model.DefaultSuitabilityEngine
import org.junit.Test

class DefaultSuitabilityEngineTest {
    val drivers = listOf(
        "Everardo Welch",
        "Orval Mayert",
        "Howard Emmerich",
        "Izaiah Lowe",
        "Monica Hermann",
        "Ellis Wisozk",
        "Noemie Murphy",
//        "Cleve Durgan",
//        "Murphy Mosciski",
//        "Kaiser Sose"
    )
    val shipments = listOf(
        "215 Osinski Manors",
        "9856 Marvin Stravenue",
        "7127 Kathlyn Ferry",
        "987 Champlin Lake",
        "63187 Volkman Garden Suite 447",
        "75855 Dessie Lights",
        "1797 Adolf Island Apt. 744",
//        "2431 Lindgren Corners",
//        "8725 Aufderhar River Suite 859",
//        "79035 Shanna Light Apt. 322"
    )

    @Test
    fun testCalculateSuitabilityMap() {
        val defaultSuitabilityEngine = DefaultSuitabilityEngine()
        defaultSuitabilityEngine.calculateSuitabilityMap(drivers, shipments)
        println("Best Permutation: ${defaultSuitabilityEngine.bestAssignments}  ${defaultSuitabilityEngine.bestAssignmentsScore}")
    }
}