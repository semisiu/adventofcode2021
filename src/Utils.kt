import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun List<Char>.binaryToDecimalNumber(): Int {
    data class State(val sum: Int, val powerOfTwo: Int)
    return reversed()
        .fold(State(0, 1)) { acc, boolean ->
            State(
                acc.sum + if (boolean == '1') acc.powerOfTwo else 0,
                acc.powerOfTwo * 2
            )
        }
        .sum
}

fun String.toListOfIntegers(delimiter: String = "") =
    if (delimiter == "") map { it.toString().toInt() }
    else split(delimiter).map { it.toInt() }

fun String.isLower() = all { it.isLowerCase() }
fun String.isUpper() = all { it.isUpperCase() }
