package day14

import readInput

data class InsertionRule(val pattern: String, val insertion: Char) {
    val newPair1 = pattern[0] + insertion.toString()
    val newPair2 = insertion.toString() + pattern[1]
}

class Polymer(
    private val pairsOccurencesCounter: MutableMap<String, Long>,
    private val rules: List<InsertionRule>,
    val totalCharsCounter: MutableMap<Char, Long> = mutableMapOf(),
) {
    init {
        rules.forEach { totalCharsCounter.putIfAbsent(it.insertion, 0L) }
    }

    fun makeStep() {
        val oldPairOccurencesCounter = pairsOccurencesCounter.toMap()
        rules
            .forEach { rule ->
                val pairCount = oldPairOccurencesCounter[rule.pattern]
                if (pairCount != null) {
                    totalCharsCounter.computeIfPresent(rule.insertion) { _, old -> old + pairCount }

                    // remove the pattern, as it was splitted
                    pairsOccurencesCounter[rule.pattern] = (pairsOccurencesCounter[rule.pattern] ?: 0) - pairCount

                    // add new pairs
                    pairsOccurencesCounter[rule.newPair1] = (pairsOccurencesCounter[rule.newPair1] ?: 0) + pairCount
                    pairsOccurencesCounter[rule.newPair2] = (pairsOccurencesCounter[rule.newPair2] ?: 0) + pairCount
                }
            }
    }

    fun min() = totalCharsCounter.values.minOrNull() ?: 0
    fun max() = totalCharsCounter.values.maxOrNull() ?: 0
}

fun main() {

    fun List<String>.parseInput(): Polymer {
        val rules = drop(2).map {
            val (pair, insertion) = it.split(" -> ", limit = 2)
            InsertionRule(pair, insertion.first())
        }

        val template: String = this[0]

        val counter = template
            .windowed(2)
            .groupBy { it }
            .mapValues { it.value.size.toLong() }
            .toMutableMap()

        val charsCounter =
            template.groupBy { it }
                .mapValues { it.value.size.toLong() }
        return Polymer(counter, rules, charsCounter.toMutableMap())
    }


    fun part1(input: List<String>, stepsCount: Int): Long {
        val polymer: Polymer = input.parseInput()

        repeat(stepsCount) { polymer.makeStep() }

        return polymer.max() - polymer.min()
    }

    fun part2(input: List<String>): Int {
        return -1
    }

    part1(readInput("day14/Day14_test"), 10).also {
        println("part1(Day14_test, 10):")
        println(it)
        check(it == 1588L)
    }

    part1(readInput("day14/Day14"), 10).also {
        println("part1(Day14, 10):")
        println(it)
        check(it == 2233L)
    }

    part1(readInput("day14/Day14_test"), 15).also {
        println("part1(Day14_test, 15):")
        println(it)
        check(it == 56892L)
    }

    part1(readInput("day14/Day14"), 40).also {
        println("part1(Day14, 40):")
        println(it)
        check(it == 2884513602164L)
    }
}
