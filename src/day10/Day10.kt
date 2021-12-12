package day10

import readInput

sealed class WrongCharacters(open val charactersLeft: String) {
    class Corrupting(val char: Char, charactersLeft: String) : WrongCharacters(charactersLeft)
    class Missing(charactersLeft: String) : WrongCharacters(charactersLeft)
}

private fun findWrongCharacters(input: String): WrongCharacters {
    tailrec fun cleanUpValidBrackets(input: String): String {
        val removed = input.replace("<>", "").replace("()", "").replace("{}", "").replace("[]", "")
        return if (removed == input) input else cleanUpValidBrackets(removed)
    }

    val cleaned = cleanUpValidBrackets(input)
    return cleaned.firstOrNull { it == ']' || it == ')' || it == '>' || it == '}' }
        ?.let { WrongCharacters.Corrupting(it, cleaned) }
        ?: WrongCharacters.Missing(cleaned)
}

fun main() {

    fun part1(input: List<String>): Int {
        val scoring = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        return input
            .map { findWrongCharacters(it) }
            .filterIsInstance<WrongCharacters.Corrupting>()
            .sumOf { scoring[it.char]!! }
    }

    fun part2(input: List<String>): Long {
        val scoring = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)
        val scores = input
            .asSequence()
            .map { findWrongCharacters(it) }
            .filterIsInstance<WrongCharacters.Missing>()
            .map { it.charactersLeft.reversed() }
            .map { it.fold(0L) { acc: Long, char: Char -> acc * 5 + scoring[char]!! } }
            .sorted()
            .toList()

        return scores[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day10/Day10_test")
    val input = readInput("day10/Day10")

    check(part1(testInput) == 26397)
    check(part1(input) == 411471)

    check(part2(testInput) == 288957L)
    check(part2(input) == 3122628974L)
}
