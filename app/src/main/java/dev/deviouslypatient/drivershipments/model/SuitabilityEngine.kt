package dev.deviouslypatient.drivershipments.model

import io.reactivex.rxjava3.core.Single

interface SuitabilityEngine {
    fun getDriverShipmentAssignments(driverNames:List<String>, shipmentDestinations:List<String>): Single<List<Assignment>>
}