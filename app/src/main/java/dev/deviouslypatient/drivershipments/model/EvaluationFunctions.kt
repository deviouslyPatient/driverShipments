package dev.deviouslypatient.drivershipments.model

import kotlin.math.abs

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

    /*
    This gcd function is an example of the Euclidean Algorithm I found online after searching around
    for a bit for advice on how to swiftly calculate common factors, I added the wrapper function
    below to ensure that we only ever consider positive factors, even though in this app we're only
    using it on string lengths which cannot be negative. i also don't really like having to consider
    the gcd when zero is involved. We shouldn't ever be dealing with empty strings, I considered
    handling that as a base case as well, but left it alone to keep these functions mathematically
    sound. I'll let data sanitization happen in a better area of the app.
     */
    private fun gcd(a: Int, b: Int): Int {
        return if (a == 0) b else gcd(b % a, a)
    }

    fun greatestCommonDivisor(a: Int, b: Int): Int {
        return if (a == 0) abs(b) else gcd(abs(a), abs(b))
    }
}
