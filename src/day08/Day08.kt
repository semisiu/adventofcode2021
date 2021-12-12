package day08

import readInput

class PatternToFourDigit(
    uniqueSignalPatterns: List<String>,
    fourDigitOutputValue: List<String>
) {
    private val uniqueSignalPatterns = uniqueSignalPatterns.map { it.toSortedSet() }.map { WrongLetterPattern(it) }
    val fourDigitOutputValue = fourDigitOutputValue.map { it.toList() }

    fun decode(): Int {
        // Find "8".
        val pattern8 = uniqueSignalPatterns.first { it.size == 7 }

        // Find "1".
        val pattern1 = uniqueSignalPatterns.first { it.size == 2 }

        // Find "7".
        val pattern7 = uniqueSignalPatterns.first { it.size == 3 }

        // a = 7 - 1
        val a = (pattern7 - pattern1).first()

        // Find "4".
        val pattern4 = uniqueSignalPatterns.first { it.size == 4 }

        // Find "3".
        val pattern3 = uniqueSignalPatterns.first { it.size == 5 && it.containsAll(pattern1) }

        // Letter "B" is "4"-"3".
        val b = (pattern4 - pattern3).first()

        // Letter "G" is "3" - "4" - "a"
        val g = (pattern3 - pattern4 - a).first()

        // e = 8 - 4 - g - a
        val e = (pattern8 - pattern4 - g - a).first()

        // d
        val d = (pattern3 - pattern1 - a - g).first()

        // Find "6"
        val pattern6 = uniqueSignalPatterns.first { it.size == 6 && e in it && d in it }

        // f
        val f = (pattern6 - a - b - d - e - g).first()

        // c
        val c = (pattern1 - f).first()

        val decoder = Decoder(a, b, c, d, e, f, g)
        return fourDigitOutputValue
            .map { decoder.decode(it) }
            .joinToString("")
            .toInt()
    }

    class WrongLetterPattern(private val letters: Set<Char>) {
        val size = letters.size
        operator fun minus(other: WrongLetterPattern) = letters - other.letters
        operator fun minus(char: Char) = letters - char
        operator fun contains(char: Char) = char in letters
        fun containsAll(other: WrongLetterPattern) = letters.containsAll(other.letters)
    }
}

class Decoder(a: Char, b: Char, c: Char, d: Char, e: Char, f: Char, g: Char) {
    private val decodingMap = mapOf(a to 'a', b to 'b', c to 'c', d to 'd', e to 'e', f to 'f', g to 'g')
    fun decode(str: List<Char>): Int {
        val decoded = str.map { decodingMap[it]!! }.toSet()
        return charsToDigit[decoded]!!
    }

    companion object {
        val charsToDigit = sortedMapOf(
            "abcefg" to 0,
            "cf" to 1,
            "acdeg" to 2,
            "acdfg" to 3,
            "bcdf" to 4,
            "abdfg" to 5,
            "abdefg" to 6,
            "acf" to 7,
            "abcdefg" to 8,
            "abcdfg" to 9,
        )
            .mapKeys { it.key.toCharArray().toSet() }
    }
}

fun main() {

    fun List<String>.parseInput(): List<PatternToFourDigit> = this
        .map { row ->
            val (uniqueSignalPatterns, fourDigitOutputValue) =
                row.split("|", limit = 2).map { it.trim() }
            uniqueSignalPatterns to fourDigitOutputValue
        }
        .map {
            val uniqueSignalPatterns = it.first.split(" ")
            val fourDigitOutputValue = it.second.split(" ")
            PatternToFourDigit(uniqueSignalPatterns, fourDigitOutputValue)
        }

    fun part1(input: List<String>): Int {
        val patternsToFourDigits: List<PatternToFourDigit> = input.parseInput()
        return patternsToFourDigits
            .flatMap { it.fourDigitOutputValue }
            .count { it.size in listOf(2, 4, 3, 7) }
    }

    fun part2(input: List<String>): Int {
        return input
            .parseInput()
            .sumOf { it.decode() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08/Day08_test")
    val input = readInput("day08/Day08")

    check(part1(testInput) == 26)
    println(part1(input))
    check(part1(input) == 255)

    check(part2(testInput) == 61229)
    println(part2(input))
    check(part2(input) == 982158)
}
