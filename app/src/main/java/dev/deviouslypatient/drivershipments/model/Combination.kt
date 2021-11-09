package dev.deviouslypatient.drivershipments.model

data class Combination(
    val driver: Driver,
    val shipment: Shipment,
    val suitibilityScore: Double)
