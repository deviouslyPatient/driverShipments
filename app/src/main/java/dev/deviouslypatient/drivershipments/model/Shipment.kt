package dev.deviouslypatient.drivershipments.model

data class Shipment(val destination: String) {
    val numberOfLetters: Int = destination.length
}
