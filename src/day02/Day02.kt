package day02

import readInput

sealed class Movement
data class UpDown(val amount: Int) : Movement()
data class Forward(val amount: Int) : Movement()

fun main() {
    fun part1(input: Sequence<Movement>): Int {
        data class State(val forwards: Int, val updowns: Int)

        val state = input
            .fold(State(0, 0)) { state, movement ->
                when (movement) {
                    is Forward -> state.copy(forwards = state.forwards + movement.amount)
                    is UpDown -> state.copy(updowns = state.updowns + movement.amount)
                }
            }

        return state.updowns * state.forwards
    }

    fun part2(input: Sequence<Movement>): Int {
        data class State(val depth: Int, val aim: Int, val horizontal: Int)

        val state = input
            .fold(State(0, 0, 0)) { state, movement ->
                when (movement) {
                    is Forward -> state.copy(
                        depth = state.depth + movement.amount * state.aim,
                        horizontal = state.horizontal + movement.amount)
                    is UpDown -> state.copy(
                        aim = state.aim + movement.amount)
                }
            }

        return state.horizontal * state.depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test").parseMovements()
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("day02/Day02").parseMovements()
    println(part1(input))
    println(part2(input))
}

private fun List<String>.parseMovements() =
    asSequence()
        .map {
            val (movement, amount) = it.trim().split(" ", limit = 2)
            when (movement) {
                "forward" -> Forward(amount.toInt())
                "up" -> UpDown(-amount.toInt())
                "down" -> UpDown(amount.toInt())
                else -> throw IllegalArgumentException("Unsupported movement $movement")
            }
        }
