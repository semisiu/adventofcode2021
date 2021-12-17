package day15

import readInput
import java.util.Comparator.comparingInt
import java.util.TreeSet

data class Vortex(
    val x: Int,
    val y: Int,
    val weight: Int,
    var cost: Int = Int.MAX_VALUE
) : Comparable<Vortex> {
    override fun compareTo(other: Vortex) =
        comparingInt<Vortex> { it.cost }
            .then(comparingInt { it.x })
            .then(comparingInt { it.y })
            .compare(this, other)
}

class Graph(private val vortices: Array<Array<Vortex>>) {
    fun solve(): Int {
        vortices.first().first().cost = 0
        val q = TreeSet<Vortex>()
        vortices.forEach { arr -> arr.forEach { q.add(it) } }

        while (q.isNotEmpty()) {
            val u = q.pollFirst()!!
            val uCost = u.cost.takeIf { it != Int.MAX_VALUE } ?: throw IllegalStateException("Expecting $u has cost")

            neighbours(u)
                .filterNotNull()
                .filter { it in q }
                .forEach { w ->
                    val newCost = uCost + w.weight
                    if (w.cost == Int.MAX_VALUE || newCost < w.cost) {
                        q.remove(w)
                        w.cost = newCost
                        q.add(w)
                    }
                }
        }

        return vortices.last().last().cost
    }

    private fun neighbours(vortex: Vortex) = sequenceOf(
        vortices.getOrNull(vortex.y)?.getOrNull(vortex.x - 1),
        vortices.getOrNull(vortex.y)?.getOrNull(vortex.x + 1),
        vortices.getOrNull(vortex.y - 1)?.getOrNull(vortex.x),
        vortices.getOrNull(vortex.y + 1)?.getOrNull(vortex.x),
    )
}


fun main() {

    fun List<String>.parseInput(): Array<Array<Vortex>> =
        mapIndexed { rowNr, row ->
            row.toCharArray()
                .mapIndexed { colNr, char -> Vortex(colNr, rowNr, char.digitToInt()) }
                .toTypedArray()
        }
            .toTypedArray()

    fun part1(input: List<String>): Int {
        val vortices = input.parseInput()
        val graph = Graph(vortices)
        return graph.solve()
    }

    fun part2(input: List<String>): Int {
        fun increaseBlock(block: String) = block
            .map { if (it in '0'..'8') it.digitToInt() + 1 else 1 }
            .joinToString("")

        val window = input
            .map { (1..4).runningFold(it) { acc, _ -> increaseBlock(acc) } }
            .map { it.joinToString("") }

        val rows = (1..4)
            .runningFold(window) { acc, _ -> acc.map { increaseBlock(it) } }
            .flatten()

        val vortices = rows.parseInput()
        val graph = Graph(vortices)
        return graph.solve()
    }

    part1(readInput("day15/Day15_test")).also {
        println("part1(Day15_test):")
        println(it)
        check(it == 40)
    }

    part1(readInput("day15/Day15")).also {
        println("part1(Day15):")
        println(it)
        check(it == 698)
    }

    part2(readInput("day15/Day15_test")).also {
        println("part1(Day15_test):")
        println(it)
        check(it == 315)
    }

    part2(readInput("day15/Day15")).also {
        println("part1(Day15):")
        println(it)
        check(it == 3022)
    }
}
