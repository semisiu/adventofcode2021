fun main() {
    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.toInt() }
            .zipWithNext()
            .count { (x, y) -> y > x }
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.toInt() }
            .zipWithNext()
            .zipWithNext()
            .map { (p1, p2) -> p1.first + p1.second + p2.second }
            .zipWithNext()
            .count { (x, y) -> y > x }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
