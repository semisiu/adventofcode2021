package aoc2020.day03

import aoc2020.day03.Matrix.FieldType.SQUARE
import aoc2020.day03.Matrix.FieldType.TREE
import readInput

data class Point(val x: Int, val y: Int) {
    fun inc(x: Int, y: Int) = Point(this.x + x, this.y + y)
}

class Matrix(private val fields: List<List<FieldType>>) {
    val rowsCount = fields.size
    private val colsCount = fields[0].size

    fun fieldAt(position: Point): FieldType = fields[position.y][position.x % colsCount]

    enum class FieldType { SQUARE, TREE }
}

fun main() {
    fun List<String>.parseInput1() = Matrix(
        this
            .filter { it.isNotBlank() }
            .map { row ->
                row.map { char ->
                    when (char) {
                        '.' -> SQUARE
                        '#' -> TREE
                        else -> TODO("unsupported char '$char'")
                    }
                }
            })

    fun part1(input: List<String>): Int {
        val matrix = input.parseInput1()

        class Result(val position: Point, val treesCount: Int)

        val stepX = 3
        val stepY = 1

        val result = (0 until matrix.rowsCount)
            .fold(Result(Point(0, 0), 0)) { acc, _ ->
                when (matrix.fieldAt(acc.position)) {
                    SQUARE ->
                        Result(acc.position.inc(stepX, stepY), acc.treesCount)
                    TREE ->
                        Result(acc.position.inc(stepX, stepY), acc.treesCount + 1)
                }
            }

        return result.treesCount
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2020/day03/Day03_test")
    check(part1(testInput) == 7)

    val input = readInput("aoc2020/day03/Day03")
    println(part1(input))
    check(part1(input) == 153)
//
//    println("part2(testInput): ${part2(testInput)}")
//    check(part2(testInput) == 1)
//    println(part2(input))
//    check(part2(input) == 306)
}
