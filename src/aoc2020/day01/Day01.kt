package aoc2020.day01

import readInput

fun main() {
    fun List<String>.parseInput() = map { it.toInt() }

    fun part1(input: List<String>): Int {
        val numbers = input.parseInput().asSequence()

        val first = numbers
            .mapIndexedNotNull { index, num1 ->
                numbers
                    .drop(index)
                    .firstOrNull { num2 -> num2 + num1 == 2020 }
            }
            .first()
        return first * (2020 - first)
    }

    fun part2(input: List<String>): Long {
        data class Triple(val a: Int, val b: Int, val c: Int) {
            val sum = a + b + c
            fun multiply() = a.toLong() * b.toLong() * c.toLong()
        }

        val numbers = input.parseInput().asSequence()

        return numbers
            .flatMapIndexed { index, a -> numbers.drop(index).map { b -> a to b } }
            .flatMap { p -> numbers.map { c -> Triple(p.first, p.second, c) } }
            .first { it.sum == 2020 }
            .multiply()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2020/day01/Day01_test")
    check(part1(testInput) == 514579)

    val input = readInput("aoc2020/day01/Day01")
    check(part1(input) == 1018944)
    println(part1(input))

    println("part2(testInput): ${part2(testInput)}")
    check(part2(testInput) == 241861950L)
    println(part2(input))
    check(part2(input) == 8446464L)
}
