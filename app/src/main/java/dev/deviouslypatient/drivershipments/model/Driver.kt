package dev.deviouslypatient.drivershipments.model

data class Driver(val name: String) {
    val numberOfVowels: Int = EvaluationFunctions.numberOfVowels(name)
    val numberOfConsonants: Int = EvaluationFunctions.numberOfConsonants(name)
    val numberOfLetters: Int = name.length
}
