package dev.deviouslypatient.drivershipments.model

import io.reactivex.rxjava3.core.Single

interface SuitabilityEngine {
    fun getDriverShipmentAssignments(driverNames:Array<String>, shipmentDestinations:Array<String>): Single<ArrayList<Combination>>
}