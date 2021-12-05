package day04

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val bingoGame = loadBoards(input)
        val chosenNumbers = input.first().splitToSequence(",").map { it.toInt() }.toList()

        val firstWinningBoard = chosenNumbers
            .flatMap { number ->
                bingoGame.markNumber(number)
                    .map { winningBoard ->
                        val sum = winningBoard.unmarkedNumbers().sum()
                        number to sum
                    }
            }
            .first()

        return firstWinningBoard.first * firstWinningBoard.second
    }

    fun part2(input: List<String>): Int {
        val bingoGame = loadBoards(input)
        val chosenNumbers: Sequence<Int> = input.first().splitToSequence(",").map { it.toInt() }

        val lastWinningBoard = chosenNumbers
            .flatMap { number ->
                bingoGame.markNumber(number)
                    .map { winningBoard ->
                        val sum = winningBoard.unmarkedNumbers().sum()
                        number to sum
                    }
            }
            .last()

        return lastWinningBoard.first * lastWinningBoard.second
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("day04/Day04")
    println(part1(input))
    println(part2(input))
    check(part1(input) == 11774)
    check(part2(input) == 4495)
}

class BingoBoard(matrixInput: List<Int>) {

    private val matrix = matrixInput
        .mapIndexed { index, value ->
            val rowNr = index / 5
            val colNr = index % 5
            Point(rowNr, colNr) to Cell(value)
        }
        .toMap()

    private var isWinning = false

    private fun cells() = matrix.values

    fun unmarkedNumbers() = cells().filterNot { it.isMarked() }.map { it.getValue() }

    // returns true if bingo!
    fun markNumber(value: Int) =
        !isWinning
            &&
            (matrix
                .filter { it.value.mark(value) }
                .map { it.key }
                .firstOrNull()
                ?.let { point: Point ->
                    isWinning = checkIfBingo(point)
                    isWinning
                } ?: false)

    private fun getCellAt(rowNr: Int, colNr: Int) = matrix[Point(rowNr, colNr)]

    private fun checkIfBingo(point: Point): Boolean {
        val markedRowNr = point.rowNr
        val markedColNr = point.colNr

        val rowMarked = (0..4).all { colNr -> getCellAt(markedRowNr, colNr)!!.isMarked() }
        val colMarked = (0..4).all { rowNr -> getCellAt(rowNr, markedColNr)!!.isMarked() }

        return rowMarked || colMarked
    }

    override fun toString() =
        (0..4).joinToString("\n") { rowNr ->
            (0..4).joinToString(" ") { colNr ->
                val cell = getCellAt(rowNr, colNr)!!
                if (cell.isMarked()) "[]"
                else "%2d".format(cell.getValue())
            }
        }

    data class Cell(private var value: Int) {
        fun getValue() = value

        fun mark(value: Int): Boolean {
            if (isMarked()) return false
            if (value == this.value) setMarked()
            return isMarked()
        }

        private fun setMarked() {
            value = -1
        }

        fun isMarked() = value == -1
    }
}

class BingoGame(private val boards: List<BingoBoard>) {
    fun markNumber(value: Int) = boards.filter { board -> board.markNumber(value) }
}

data class Point(val rowNr: Int, val colNr: Int)

private fun loadBoards(input: List<String>): BingoGame {
    val split = input
        .asSequence()
        .drop(2)
        .filterNot { it.isEmpty() }
        .joinToString(" ")
        .replace("  ", " ")
        .replace("   ", " ")
        .split(" ")
    return split
        .map { it.toInt() }
        .chunked(25)
        .map { BingoBoard(it) }
        .let { BingoGame(it.toList()) }
}
