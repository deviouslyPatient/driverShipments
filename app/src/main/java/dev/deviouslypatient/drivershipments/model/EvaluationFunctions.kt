package dev.deviouslypatient.drivershipments.model

object EvaluationFunctions {
    // todo verify if Y counts as a vowel or a consonant, or both, or conditionally one or the other
    private val VOWELS = listOf('A','E','I','O','U','Y')
    private val CONSONANTS = listOf('B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','W','X','Z')

    fun numberOfVowels(input: String?): Int {
        return input?.trim()?.uppercase()?.filter {VOWELS.contains(it)}?.count() ?: 0
    }

    fun numberOfConsonants(input: String?): Int {
        return input?.trim()?.uppercase()?.filter {CONSONANTS.contains(it)}?.count() ?: 0
    }

    fun greatestCommonDivisor(a: Int, b: Int): Int {
        return if (a == 0 || b == 0) 0 else gcd(a, b)
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (a == 0) b else gcd(b % a, a)
    }
}
