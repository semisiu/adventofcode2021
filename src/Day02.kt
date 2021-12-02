fun main() {
    fun part1(input: List<String>): Int {
        return input
            .parseOperations()
            .groupBy { it.javaClass }
            .mapValues { it.value.sumOf { movement -> movement.amount } }
            .map { it.value }
            .reduce { a, b -> a * b }
    }

    fun part2(input: List<String>): Int {
        class State(val depth: Int, val aim: Int, val horizontal: Int)
        val state = input
            .parseOperations()
            .fold(State(0, 0, 0)) { state, movement ->
                when (movement) {
                    is Forward -> {
                        State(
                            state.depth + movement.amount * state.aim,
                            state.aim,
                            state.horizontal + movement.amount)
                    }
                    is UpDown -> {
                        State(
                            state.depth,
                            state.aim + movement.amount,
                            state.horizontal)
                    }
                }
            }

        return state.horizontal * state.depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

sealed class Movement(open val amount: Int)
data class UpDown(override val amount: Int) : Movement(amount)
data class Forward(override val amount: Int) : Movement(amount)

private fun List<String>.parseOperations(): Sequence<Movement> =
    asSequence()
        .map {
            val (operation, amount) = it.split(" ")
            when (operation) {
                "forward" -> Forward(amount.toInt())
                "up" -> UpDown(-amount.toInt())
                "down" -> UpDown(amount.toInt())
                else -> throw IllegalArgumentException("Unsupported movement $operation")
            }
        }
