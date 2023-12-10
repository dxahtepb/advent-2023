fun main() {
    val december1 = December1()
    december1.first()
    december1.second()
}

class December1 : Solution() {
    override fun first() {
        readLines("first.txt").sumOf {
            val firstDigit = it.first(Char::isDigit)
            val lastDigit = it.last(Char::isDigit)
            "$firstDigit$lastDigit".toInt()
        }.also { println(it) }
    }

    override fun second() {
        val digitToChar = mapOf(
            "0" to 0, "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9,
            "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
        )
        readLines("first.txt").sumOf { line ->
            val first = digitToChar.keys
                .map { it to line.indexOf(it) }
                .filter { it.second != -1 }
                .minByOrNull { it.second }!!
                .first
            val last = digitToChar.keys
                .map { it to line.lastIndexOf(it) }
                .filter { it.second != -1 }
                .maxByOrNull { it.second }!!
                .first
            "${digitToChar[first]}${digitToChar[last]}".toInt()
        }.also { println(it) }
    }
}
