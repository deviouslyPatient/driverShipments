package dev.deviouslypatient.drivershipments.data

import dev.deviouslypatient.drivershipments.model.Assignment


interface SuitabilityEngine {
    fun calculateDriverShipmentAssignments(driverNames:List<String>, shipmentDestinations:List<String>): List<Assignment>
}