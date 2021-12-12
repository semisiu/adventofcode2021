package day09

import readInput

class Locations(locations: List<List<Int>>) {
    class Cell(val x: Int, val y: Int, val value: Int, var visited: Boolean = false)

    private val rows = locations.mapIndexed { rowNr, row ->
        row.mapIndexed { colNr, value ->
            Cell(colNr, rowNr, value)
        }
    }

    private val height = rows.size
    private val width = rows.first().size

    fun findLowPoints() =
        rows.asSequence()
            .flatten()
            .filter { cell -> adjacents(cell).all { it.value > cell.value } }

    fun getBasinsSizes() = findLowPoints().map { getBasin(it) }

    private fun cellAt(x: Int, y: Int) =
        if (x >= 0 && y >= 0 && y < height && x < width) rows[y][x]
        else null

    private fun adjacents(cell: Cell): List<Cell> = listOfNotNull(
        cellAt(cell.x, cell.y - 1),
        cellAt(cell.x, cell.y + 1),
        cellAt(cell.x - 1, cell.y),
        cellAt(cell.x + 1, cell.y),
    )

    private fun getBasin(point: Cell): Int {
        if (point.visited) return 0
        point.visited = true
        val sum = adjacents(point).filter { it.value < 9 }.sumOf { getBasin(it) }
        return sum + 1
    }
}

fun main() {
    fun List<String>.parseInput() = map { row -> row.map { char -> char.toString().toInt() } }

    fun part1(input: List<String>): Int {
        val locations = Locations(input.parseInput())
        return locations
            .findLowPoints()
            .sumOf { it.value + 1 }
    }

    fun part2(input: List<String>): Int {
        val locations = Locations(input.parseInput())
        return locations
            .getBasinsSizes()
            .toList()
            .sorted()
            .takeLast(3)
            .reduce { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day09/Day09_test")
    val input = readInput("day09/Day09")

    check(part1(testInput) == 15)
    println(part1(input))
    check(part1(input) == 516)

    check(part2(testInput) == 1134)
    println(part2(input))
    check(part2(input) == 1023660)
}
