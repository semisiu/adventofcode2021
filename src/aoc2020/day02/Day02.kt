package aoc2020.day02

import readInput

class PasswordPolicy(private val index1: Int, private val index2: Int, private val char: Char) {
    private val range = index1..index2
    fun passwordConforms1(password: String) = password.count { it == char } in range
    fun passwordConforms2(password: String): Boolean {
        val char1 = password[index1 - 1]
        val char2 = password[index2 - 1]
        return char1 != char2 && (char1 == char || char2 == char)
    }
}

fun main() {

    fun List<String>.parseInput(): List<Pair<PasswordPolicy, String>> =
        map { row ->
            val (policy, password) = row.split(": ", limit = 2)
            val (range, char) = policy.split(" ", limit = 2)
            val (rangeFrom, rangeTo) = range.split("-", limit = 2).map { it.toInt() }
            val passwordPolicy = PasswordPolicy(rangeFrom, rangeTo, char.first())
            passwordPolicy to password
        }

    fun part1(input: List<String>): Int {
        return input.parseInput()
            .count { (policy, password) ->
                policy.passwordConforms1(password)
            }
    }

    fun part2(input: List<String>): Int {
        return input.parseInput()
            .count { (policy, password) ->
                policy.passwordConforms2(password)
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2020/day02/Day02_test")
    check(part1(testInput) == 2)

    val input = readInput("aoc2020/day02/Day02")
    check(part1(input) == 572)
    println(part1(input))

    println("part2(testInput): ${part2(testInput)}")
    check(part2(testInput) == 1)
    println(part2(input))
    check(part2(input) == 306)
}
