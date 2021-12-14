package day13

import readInput

class Paper(
    private val width: Int,
    private val height: Int,
    coords: Collection<Pair<Int, Int>>
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
        coords = coords.map { it.foldY(line) }
    )

    private fun Pair<Int, Int>.foldX(line: Int) =
        if (first > line) copy(first = line * 2 - first) else this

    private fun Pair<Int, Int>.foldY(line: Int) =
        if (second > line) copy(second = line * 2 - second) else this

    override fun toString() =
        (0..height).joinToString("\n") { y ->
            (0..width).joinToString("") { x ->
                if (x to y in coords) "#" else " "
            }
        }
}

sealed class Along {
    abstract val line: Int

    data class X(override val line: Int) : Along()
    data class Y(override val line: Int) : Along()
}

fun main() {

    fun List<String>.parseInput(): Pair<Paper, List<Along>> {
        val emptyLineIndex = indexOfFirst { it.isEmpty() }
        val coords = take(emptyLineIndex).map {
            val (x, y) = it.split(",", limit = 2)
            x.toInt() to y.toInt()
        }

        val instructions = drop(emptyLineIndex + 1).map {
            val (axis, amount) = it.split("=", limit = 2)
            if (axis == "fold along x") Along.X(amount.toInt())
            else Along.Y(amount.toInt())
        }

        val width = coords.maxOf { it.first } + 1
        val height = coords.maxOf { it.second } + 1

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
