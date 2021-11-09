package dev.deviouslypatient.drivershipments.model

data class Assignment(
    val driver: String,
    val shipment: String?,
    val suitibilityScore: Double)
