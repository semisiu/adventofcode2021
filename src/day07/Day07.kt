package day07

import readInput
import kotlin.math.absoluteValue

class Positions(private val values: List<Int>) {
    private val min = values.minOrNull()!!
    private val max = values.maxOrNull()!!

    fun minFuelCost1() = (min..max).minOf { target -> fuelCost1(target) }
    private fun fuelCost1(target: Int) = values.sumOf { it.distance(target) }

    fun minFuelCost2() = (min..max).minOf { target -> fuelCost2(target) }
    private fun fuelCost2(target: Int) = values.sumOf {
        val distance = target.distance(it)
        distance * (distance + 1) / 2
    }

    private fun Int.distance(target: Int) = (this - target).absoluteValue
}

fun main() {

    fun part1(input: List<String>) =
        Positions(input.first().split(",").map { it.toInt() })
            .minFuelCost1()

    fun part2(input: List<String>) =
        Positions(input.first().split(",").map { it.toInt() })
            .minFuelCost2()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    val input = readInput("day07/Day07")

    check(part1(testInput) == 37)
    println(part1(input))
    check(part1(input) == 340052)

    check(part2(testInput) == 168)
    println(part2(input))
    check(part2(input) == 92948968)
}

