package day05

import readInput
import kotlin.math.absoluteValue
import kotlin.math.max

fun main() {
    fun loadLines(input: List<String>) =
        input
            .map { row ->
                val (p1, p2) = row.split(" -> ", limit = 2)
                    .map { coord ->
                        val (x, y) = coord.split(",", limit = 2).map { it.toInt() }
                        Point(x, y)
                    }
                Line(p1..p2)
            }

    fun part1(input: List<String>): Int {
        val lines = loadLines(input).filter { it.isHorizontal || it.isVertical }
        return Diagram(lines).matrix.count { it.value > 1 }
    }

    fun part2(input: List<String>): Int {
        val lines = loadLines(input)
        return Diagram(lines).matrix.count { it.value > 1 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("day05/Day05")
    println(part1(input))
    println(part2(input))
    check(part1(input) == 7468)
    check(part2(input) == 22364)
}

class Diagram(private val lines: List<Line>) {
    val matrix: Map<Point, Int> = lines
        .flatMap { it.points }
        .groupBy { it }
        .mapValues { it.value.size }
}

data class Point(val x: Int, val y: Int) {
    operator fun rangeTo(end: Point): List<Point> {
        val deltaX = end.x - x
        val deltaY = end.y - y

        val step = Point(deltaX.signum(), deltaY.signum())

        return (1..max(deltaX.absoluteValue, deltaY.absoluteValue))
            .runningFold(this) { acc, _ -> acc + step }
    }

    private operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}
private fun Int.signum() =
    when {
        this == 0 -> 0
        this < 0 -> -1
        else -> 1
    }

data class Line(val points: List<Point>) {
    val isHorizontal: Boolean by lazy { points.first().y == points.last().y }
    val isVertical: Boolean by lazy { points.first().x == points.last().x }
}
