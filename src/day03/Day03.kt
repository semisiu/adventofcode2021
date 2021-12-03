package day03

import readInput
import toDecimalNumber

data class DiagnosticResult(var bits: List<Boolean>) {
    override fun toString() = bits.joinToString("") { if (it) "1" else "0" }
}

class WinnersAccumulator(val onesCount: List<Int>, val zerosCount: List<Int>) {
    operator fun plus(other: WinnersAccumulator) =
        if (this == Empty) other
        else WinnersAccumulator(
            onesCount.zip(other.onesCount) { x, y -> x + y },
            zerosCount.zip(other.zerosCount) { x, y -> x + y }
        )

    fun onesWinBits(): List<Boolean> = onesCount.zip(zerosCount) { ones, zeros -> ones > zeros }

    fun zerosWinBits(): List<Boolean> = onesCount.zip(zerosCount) { ones, zeros -> zeros > ones }

    companion object {
        val Empty = WinnersAccumulator(emptyList(), emptyList())
    }
}

fun main() {

    fun part1(input: List<DiagnosticResult>): Int {
        val summary = input
            .fold(WinnersAccumulator.Empty) { acc, diagnosticResult ->
                val (ones, zeros) = diagnosticResult
                    .bits
                    .map { if (it) 1 to 0 else 0 to 1 }
                    .unzip()

                acc + WinnersAccumulator(ones, zeros)
            }

        data class ResultAcc(val gamma: Int, val epsilon: Int, val powerOfTwo: Int)

        val resultAcc = summary
            .onesWinBits()
            .reversed()
            .fold(ResultAcc(0, 0, 1)) { acc, onesWin ->
                if (onesWin)
                    acc.copy(
                        gamma = acc.gamma + acc.powerOfTwo,
                        powerOfTwo = acc.powerOfTwo * 2
                    ) else
                    acc.copy(
                        epsilon = acc.epsilon + acc.powerOfTwo,
                        powerOfTwo = acc.powerOfTwo * 2
                    )
            }
        return resultAcc.gamma * resultAcc.epsilon
    }

    fun part2(input: List<DiagnosticResult>): Int {
        tailrec fun oxygen(input: List<DiagnosticResult>, bitNumber: Int): DiagnosticResult {
            if (input.size == 1) {
                return input[0]
            }

            val groupBy: Map<Boolean, List<DiagnosticResult>> = input.groupBy { it.bits[bitNumber] }

            val ones = groupBy.getOrDefault(true, emptyList())
            val zeros = groupBy.getOrDefault(false, emptyList())

            val keep: List<DiagnosticResult> = if (ones.size >= zeros.size) {
                ones
            } else {
                zeros
            }

            return oxygen(keep, bitNumber + 1)
        }

        tailrec fun co2(input: List<DiagnosticResult>, bitNumber: Int): DiagnosticResult {
            if (input.size == 1) {
                return input[0]
            }

            val groupBy: Map<Boolean, List<DiagnosticResult>> = input.groupBy { it.bits[bitNumber] }

            val ones = groupBy.getOrDefault(true, emptyList())
            val zeros = groupBy.getOrDefault(false, emptyList())

            val keep: List<DiagnosticResult> = if (zeros.size <= ones.size) {
                zeros
            } else {
                ones
            }

            return co2(keep, bitNumber + 1)
        }

        val oxygen = oxygen(input, 0).bits.toDecimalNumber()
        val co2 = co2(input, 0).bits.toDecimalNumber()

        return oxygen * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test").parseDiagnosticReport()
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("day03/Day03").parseDiagnosticReport()
    check(part1(input) == 3882564)
    println(part1(input))
    println(part2(input))
}

private fun List<String>.parseDiagnosticReport(): List<DiagnosticResult> =
    map { line ->
        DiagnosticResult(
            line
                .toCharArray()
                .map { it == '1' }
                .toList())
    }

