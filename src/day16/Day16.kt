package day16

import day16.Decoder.binaryToDecimal
import day16.Decoder.hexToBinary
import day16.Packet.Operator
import readInput

object Decoder {
    private val hexToBinaryMap = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111",
    )

    fun String.hexToBinary() = this.map { hexToBinaryMap[it] }.joinToString("")
    fun String.binaryToDecimal(): Long {
        val result = this.reversed().fold(0L to 1L) { acc, char ->
            val powerOfTwo = acc.second
            val sum = if (char == '1') acc.first + powerOfTwo else acc.first
            sum to powerOfTwo * 2
        }
        return result.first
    }
}

sealed class Packet {
    abstract fun versionsSum(): Int
    abstract fun execute(): Long

    data class Literal(val version: Int, val value: Long) : Packet() {
        override fun versionsSum() = version
        override fun execute() = value
    }

    data class Operator(val version: Int, val type: Int, val subPackets: List<Packet>) : Packet() {
        override fun versionsSum() = version + subPackets.sumOf { it.versionsSum() }
        override fun execute() = when (type) {
            0 -> subPackets.sumOf { it.execute() }
            1 -> subPackets.map { it.execute() }.reduce { acc, value -> acc * value }
            2 -> subPackets.minOf { it.execute() }
            3 -> subPackets.maxOf { it.execute() }
            5 -> if (subPackets[0].execute() > subPackets[1].execute()) 1 else 0
            6 -> if (subPackets[0].execute() < subPackets[1].execute()) 1 else 0
            7 -> if (subPackets[0].execute() == subPackets[1].execute()) 1 else 0
            else -> throw IllegalStateException("Unknown type: $type")
        }
    }
}

fun extractPacket(input: String): Pair<Int, Packet> {
    val version = input.substring(0, 3).binaryToDecimal().toInt()
    when (val type = input.substring(3, 6).binaryToDecimal().toInt()) {
        4 -> {
            val chunks = input.substring(6, input.length).chunked(5)
            val lastChunk = chunks.indexOfFirst { chunk -> chunk[0] == '0' }
            val fives = chunks.take(lastChunk + 1)

            val number = fives
                .map { it.drop(1) }
                .joinToString("")
                .binaryToDecimal()

            val nextPosition = 3 + 3 + 5 * (lastChunk + 1)
            return nextPosition to Packet.Literal(version, value = number)
        }
        else ->
            when (val lengthTypeId = input.substring(6, 7)) {
                "0" -> {
                    val subPacketsLength = input.substring(7, 22).binaryToDecimal().toInt()

                    val stopAt = 22 + subPacketsLength
                    val subPackets = sequence {
                        var i = 22
                        while (i < stopAt) {
                            val (nextPosition, packet) = extractPacket(input.substring(i, input.length))
                            yield(packet)
                            i += nextPosition
                        }
                    }.toList()

                    return stopAt to Operator(version, type, subPackets)
                }
                "1" -> {
                    val subPacketsCount = input.substring(7, 18).binaryToDecimal()

                    var currentPosition = 18

                    val subPackets = sequence {
                        repeat(subPacketsCount.toInt()) {
                            val (nextPosition, packet) = extractPacket(input.substring(currentPosition, input.length))
                            yield(packet)
                            currentPosition += nextPosition
                        }
                    }.toList()

                    return currentPosition to Operator(version, type, subPackets)
                }
                else -> throw IllegalStateException("Unknown length type id $lengthTypeId")
            }
    }
}

fun main() {

    fun part1(input: String): Int {
        val packet = extractPacket(input.hexToBinary()).second
        return packet.versionsSum()
    }

    fun part2(input: String): Long {
        val packet = extractPacket(input.hexToBinary()).second
        return packet.execute()
    }

    sequenceOf(
        "8A004A801A8002F478" to 16,
        "620080001611562C8802118E34" to 12,
        "C0015000016115A2E0802F182340" to 23,
        "A0016C880162017C3686B18A3D4780" to 31,
        readInput("day16/Day16").first() to 984,
    ).forEach { (encoded, expectedSum) ->
        val result = part1(encoded)
        check(result == expectedSum) { "Expecting $encoded results in $expectedSum" }
    }

    sequenceOf(
        "C200B40A82" to 3L,
        "04005AC33890" to 54L,
        "880086C3E88112" to 7L,
        "CE00C43D881120" to 9L,
        "D8005AC2A8F0" to 1L,
        "F600BC2D8F" to 0L,
        "9C005AC2F8F0" to 0L,
        "9C0141080250320F1802104A08" to 1L,
        readInput("day16/Day16").first() to 1015320896946L,
    ).forEach { (encoded, expectedResult) ->
        val result = part2(encoded)
        check(result == expectedResult) { "Expecting $encoded results in $expectedResult" }
    }
}
