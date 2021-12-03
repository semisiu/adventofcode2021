fun List<Boolean>.toDecimalNumber(): Int {
    data class State(val sum: Int, val powerOfTwo: Int)
    return reversed()
        .fold(State(0, 1)) { acc, boolean ->
            State(
                acc.sum + if (boolean) acc.powerOfTwo else 0,
                acc.powerOfTwo * 2
            )
        }
        .sum
}

fun main() {
    val toDecimalNumber = listOf(true, true, false, true).toDecimalNumber()
    println(toDecimalNumber)
}
