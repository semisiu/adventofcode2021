package day11

import readInput
import toListOfIntegers

class Octopus(val x: Int, val y: Int, var energyLevel: Int) {
    private val incrementedBy = mutableSetOf<Octopus>()
    fun flashes() = energyLevel == 10 || energyLevel == 0

    fun reset() {
        if (flashes()) energyLevel = 0
        incrementedBy.clear()
    }

    fun increaseEnergyLevelBy1() {
        energyLevel++
    }

    fun increaseEnergyLevel(by: Octopus) =
        if (!flashes() && incrementedBy.add(by)) {
            increaseEnergyLevelBy1()
            true
        } else false
}

class Cavern(octopusesList: List<List<Int>>) {
    val octopuses = octopusesList
        .mapIndexed { rowNr: Int, row: List<Int> ->
            row.mapIndexed { colNr, energyLevel ->
                Octopus(colNr, rowNr, energyLevel)
            }
        }

    private val height = octopuses.size
    private val width = octopuses.first().size

    fun makeStep(): Int {
        increaseEnergyLevelsBy1()
        (1..9999).first { !affectAdjacents() }
        val count = octopuses.flatten().count { it.flashes() }
        octopuses.flatten().forEach { it.reset() }
        return count
    }

    @Suppress("SimplifiableCallChain")
    private fun increaseEnergyLevelsBy1() = octopuses.flatten().forEach { it.increaseEnergyLevelBy1() }

    private fun affectAdjacents(): Boolean =
        octopuses.flatten()
            .filter { it.flashes() }
            .any { octopus ->
                adjacents(octopus).any { it.increaseEnergyLevel(octopus) }
            }

    private fun adjacents(octopus: Octopus) = with(octopus) {
        listOfNotNull(
            octopusAt(x - 1, y - 1),
            octopusAt(x - 1, y),
            octopusAt(x - 1, y + 1),
            octopusAt(x, y - 1),
            octopusAt(x, y + 1),
            octopusAt(x + 1, y - 1),
            octopusAt(x + 1, y),
            octopusAt(x + 1, y + 1),
        )
    }

    private fun octopusAt(x: Int, y: Int) =
        if (x in 0 until width && y in 0 until height) octopuses[y][x]
        else null

    fun allFlashes() = octopuses.flatten().all { it.flashes() }
}

fun main() {

    fun part1(input: List<String>, steps: Int): Int {
        val cavern = Cavern(input.map { row -> row.toListOfIntegers() })

        return (1..steps).sumOf { cavern.makeStep() }
    }

    fun part2(input: List<String>): Int {
        val cavern = Cavern(input.map { row -> row.toListOfIntegers() })
        return (1..9999)
            .first {
                cavern.makeStep()
                cavern.allFlashes()
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day11/Day11_test")
    val input = readInput("day11/Day11")

    check(part1(testInput, 100) == 1656)
    check(part1(input, 100) == 1644)

    check(part2(testInput) == 195)
    check(part2(input) == 229)
}
