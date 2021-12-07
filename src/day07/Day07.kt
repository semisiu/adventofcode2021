package day07

import readInput
import kotlin.math.absoluteValue

class Positions(private val values: List<Int>) {
    private val min = values.minOrNull()!!
    private val max = values.maxOrNull()!!

    fun fuelCost1() = (min..max).minOf { fuelCost1(it) }
    private fun fuelCost1(target: Int) = values.sumOf { it.distance(target) }

    fun fuelCost2() = (min..max).minOf { fuelCost2(it) }
    private fun fuelCost2(target: Int) = values.sumOf {
        val distance = target.distance(it)
        distance * (distance + 1) / 2
    }

    private fun Int.distance(target: Int) = (this - target).absoluteValue
}

fun main() {

    fun part1(input: List<String>): Int {
        val positions = Positions(input[0].split(",").map { it.toInt() })
        return positions.fuelCost1()
    }

    fun part2(input: List<String>): Int {
        val positions = Positions(input[0].split(",").map { it.toInt() })
        return positions.fuelCost2()
    }

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

