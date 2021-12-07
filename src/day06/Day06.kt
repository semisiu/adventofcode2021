package day06

import readInput

class Fishes(private val fishesCountByTimer: LongArray) {

    fun total() = fishesCountByTimer.sum()

    fun moveNextDay() {
        val old0 = fishesCountByTimer[0]
        repeat(8) { fishesCountByTimer[it] = fishesCountByTimer[it + 1] }
        fishesCountByTimer[6] += old0
        fishesCountByTimer[8] = old0
    }
}

fun main() {

    fun part1(input: List<String>, days: Int): Long {
        val timers = input[0].split(",").map { it.toInt() }
        return Fishes(
            (0..8)
                .map { timers.count(it::equals).toLong() }
                .toLongArray()
        )
            .apply { repeat(days) { moveNextDay() } }
            .total()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/Day06_test")
    val input = readInput("day06/Day06")

    check(part1(testInput, 18) == 26L)
    check(part1(testInput, 80) == 5934L)
    println(part1(input, 80))
    check(part1(input, 80) == 374927L)

    check(part1(testInput, 256) == 26984457539L)
    println(part1(input, 256))
    check(part1(input, 256) == 1687617803407)
}
