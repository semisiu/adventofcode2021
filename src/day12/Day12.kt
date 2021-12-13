package day12

import isLower
import isUpper
import readInput

private typealias Path = List<String>

private fun pathOf(node: String) = listOf(node)

data class Graph(private val edges: Map<String, Set<String>>) {

    fun findPathsPart1(
        node: String = "start",
        visitedNodes: Set<String> = emptySet(),
    ): List<Path> =
        if (node == "end") listOf(pathOf("end"))
        else {
            val newVisitedNodes = if (node.isUpper()) visitedNodes else visitedNodes + node
            edges[node].orEmpty()
                .filterNot { it in visitedNodes }
                .flatMap { findPathsPart1(it, newVisitedNodes) }
                .map { pathOf(node) + it }
        }

    fun findPathsPart2(
        node: String = "start",
        visitCounter: Map<String, Int> = emptyMap(),
    ): List<Path> =
        if (node == "end") listOf(pathOf("end"))
        else {
            val newVisitCounter =
                if (node.isUpper()) visitCounter
                else {
                    val nodeCount = visitCounter.getOrDefault(node, 0)
                    visitCounter + (node to nodeCount + 1)
                }
            edges[node].orEmpty()
                .filterNot {
                    val anyVisitedTwice = newVisitCounter.any { n -> n.value >= 2 }
                    anyVisitedTwice && it in newVisitCounter
                }
                .flatMap { findPathsPart2(it, newVisitCounter) }
                .map { pathOf(node) + it }
        }

    companion object {
        fun of(edges: List<Pair<String, String>>) = Graph(edges
            .flatMap { pair -> sequenceOf(pair, pair.second to pair.first) }
            .filterNot { it.first == "end" || it.second == "start" }
            .groupBy { it.first }
            .mapValues { entry -> entry.value.map { it.second }.toSortedSet() }
            .toSortedMap()
        )
    }
}

fun main() {

    fun List<String>.parseInput() =
        filter { it.isNotBlank() }
            .map {
                val (a, b) = it.split("-", limit = 2)
                a to b
            }

    fun part1(input: List<String>): Int {
        val graph = Graph.of(input.parseInput())
        return graph
            .findPathsPart1()
            .filter { (it - "start" - "end").any(String::isLower) }
            .size
    }

    fun part2(input: List<String>): Int {
        val graph = Graph.of(input.parseInput())
        return graph
            .findPathsPart2()
            .size
    }

    part1(readInput("day12/Day12_testA")).also {
        println("part1(Day12_testA):")
        println(it)
        check(it == 9)
    }

    part1(readInput("day12/Day12_test")).also {
        println("part1(Day12_test):")
        println(it)
        check(it == 18)
    }

    part1(readInput("day12/Day12")).also {
        println("part1(Day12):")
        println(it)
        check(it == 3856)
    }

    part2(readInput("day12/Day12_testA")).also {
        println("part2(Day12_testA):")
        println(it)
        check(it == 36)
    }

    part2(readInput("day12/Day12_test")).also {
        println("part2(Day12_test):")
        println(it)
        check(it == 103)
    }

    part2(readInput("day12/Day12")).also {
        println("part2(Day12):")
        println(it)
        check(it == 116692)
    }
}
