package day13

import readInput

data class Point(val x: Int, val y: Int) {
    fun foldX(line: Int) = if (x > line) copy(x = line * 2 - x) else this
    fun foldY(line: Int) = if (y > line) copy(y = line * 2 - y) else this
}

class Paper(
    private val width: Int,
    private val height: Int,
    coords: Collection<Point>
) {
    private val coords = coords.toSet()

    fun countDots() = coords.size

    fun fold(instruction: Along) = when (instruction) {
        is Along.X -> foldAlongX(instruction.line)
        is Along.Y -> foldAlongY(instruction.line)
    }

    private fun foldAlongX(line: Int) = Paper(
        width = line,
        height = height,
        coords = coords.map { it.foldX(line) }
    )

    private fun foldAlongY(line: Int) = Paper(
        width = width,
        height = line,
        coords = this.coords.map { it.foldY(line) }
    )

    override fun toString() = (0..height).joinToString("\n") { y ->
        (0..width).joinToString("") { x ->
            if (Point(x, y) in coords) "#" else " "
        }
    }
}

sealed class Along(open val line: Int) {
    class X(override val line: Int) : Along(line)
    class Y(override val line: Int) : Along(line)
}

fun main() {

    fun List<String>.parseInput(): Pair<Paper, List<Along>> {
        val emptyLineIndex = indexOfFirst { it.isEmpty() }
        val coords = take(emptyLineIndex).map {
            val (x, y) = it.split(",", limit = 2)
            Point(x.toInt(), y.toInt())
        }

        val instructions = drop(emptyLineIndex + 1).map {
            val (axis, amount) = it.split("=", limit = 2)
            if (axis == "fold along x") Along.X(amount.toInt())
            else Along.Y(amount.toInt())
        }

        val width = coords.maxOf { it.x } + 1
        val height = coords.maxOf { it.y } + 1

        return Paper(width, height, coords) to instructions
    }

    fun part1(input: List<String>): Int {
        val (paper, instructions) = input.parseInput()
        return paper
            .fold(instructions.first())
            .countDots()
    }

    fun part2(input: List<String>): Paper {
        val (paper, instructions) = input.parseInput()
        return instructions.fold(paper, Paper::fold)
    }

    part1(readInput("day13/Day13_test")).also {
        println("part1(Day13_test):")
        println(it)
        check(it == 17)
    }

    part1(readInput("day13/Day13")).also {
        println("part1(Day13):")
        println(it)
        check(it == 759)
    }

    part2(readInput("day13/Day13_test")).also {
        println("part2(Day13_test):")
        println("$it\n")
    }

    part2(readInput("day13/Day13")).also {
        println("part2(Day13):")
        println("$it\n")
    }
}
