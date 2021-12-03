package day03

import readInput
import toDecimalNumber

data class DiagnosticResult(var bits: List<Char>)

fun main() {

    fun part1(input: List<DiagnosticResult>): Int {
        class WinnersAccumulator(val onesCount: List<Int>, val zerosCount: List<Int>) {
            operator fun plus(other: WinnersAccumulator) =
                if (onesCount.isEmpty()) other
                else WinnersAccumulator(
                    onesCount.zip(other.onesCount) { x, y -> x + y },
                    zerosCount.zip(other.zerosCount) { x, y -> x + y }
                )

        }

        val summary = input
            .fold(WinnersAccumulator(emptyList(), emptyList())) { acc, diagnosticResult ->
                val (ones, zeros) = diagnosticResult
                    .bits
                    .map { if (it == '1') 1 to 0 else 0 to 1 }
                    .unzip()

                acc + WinnersAccumulator(ones, zeros)
            }

        data class GammaEpsilonAcc(val gamma: Int, val epsilon: Int, val powerOfTwo: Int)

        val resultAcc = summary
            .onesCount
            .zip(summary.zerosCount) { onesCount, zerosCount -> onesCount > zerosCount }
            .reversed()
            .fold(GammaEpsilonAcc(0, 0, 1)) { acc, onesWin ->
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
        tailrec fun calculateRating(
            input: List<DiagnosticResult>,
            bitNumber: Int,
            predicate: (List<DiagnosticResult>, List<DiagnosticResult>) -> Boolean
        ): DiagnosticResult {
            if (input.size == 1) {
                return input[0]
            }

            val groupBy = input.groupBy { it.bits[bitNumber] }

            val ones = groupBy.getOrDefault('1', emptyList())
            val zeros = groupBy.getOrDefault('0', emptyList())

            val keep = if (predicate(ones, zeros)) ones else zeros
            return calculateRating(keep, bitNumber + 1, predicate)
        }

        val oxygenPredicate: (List<DiagnosticResult>, List<DiagnosticResult>) -> Boolean = { ones, zeros ->
            ones.size >= zeros.size
        }
        val co2Predicate: (List<DiagnosticResult>, List<DiagnosticResult>) -> Boolean = { ones, zeros ->
            ones.size < zeros.size
        }

        val oxygen = calculateRating(input, 0, oxygenPredicate).bits.toDecimalNumber()
        val co2 = calculateRating(input, 0, co2Predicate).bits.toDecimalNumber()

        return oxygen * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test").parseDiagnosticReport()
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("day03/Day03").parseDiagnosticReport()
    check(part1(input) == 3882564)
    check(part2(input) == 3385170)
    println(part1(input))
    println(part2(input))
}

private fun List<String>.parseDiagnosticReport(): List<DiagnosticResult> =
    map { DiagnosticResult(it.toCharArray().toList()) }

