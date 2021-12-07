package day07

import readInput
import kotlin.math.absoluteValue

class Positions(private val values: List<Int>) {
    fun fuelCost1(target: Int) = values.sumOf { it.distance(target) }
    fun fuelCost2(target: Int) = values.sumOf {
        val diff = target.distance(it)
        diff * (diff + 1) / 2
    }

    private fun Int.distance(target: Int) = (this - target).absoluteValue
}

fun main() {

    fun part1(input: List<String>): Int {
        val positions = Positions(input[0].split(",").map { it.toInt() })
        return (0..2000).minOf { positions.fuelCost1(it) }
    }

    fun part2(input: List<String>): Int {
        val positions = Positions(input[0].split(",").map { it.toInt() })
        return (0..2000).minOf { positions.fuelCost2(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    val input = readInput("day07/Day07")

    check(part1(testInput) == 37)
    println(part1(input))
    check(part1(input) == 340052)

    val part2TestInput = part2(testInput)
    println("part2(testInput): $part2TestInput")
    check(part2TestInput == 168)
    println(part2(input))
    check(part2(input) == 92948968)

}

