import kotlin.math.pow

fun main() {
    December4().run()
}

class December4 : Solution() {

    private data class Card(val myNumbers: Set<Int>, val winNumbers: Set<Int>)

    private fun parseNumbers(myNumbersRaw: String) = myNumbersRaw.split(Regex("\\s+")).map(String::toInt).toSet()

    private fun parseCard(rawCard: String): Card {
        val (_, winNumbersRaw, myNumbersRaw) = rawCard.split(":", "|").map(String::trim)
        val myNumbers = parseNumbers(myNumbersRaw)
        val winNumbers = parseNumbers(winNumbersRaw)
        return Card(myNumbers, winNumbers)
    }

    private fun countCardValue(card: Card): Int {
        val matchingNumbers = card.myNumbers.intersect(card.winNumbers)
        return if (matchingNumbers.isEmpty()) {
            0
        } else {
            return 2.0.pow(matchingNumbers.size - 1).toInt()
        }

    }

    override fun first() {
        readLines("fourth.txt")
            .map(this::parseCard)
            .sumOf { card -> countCardValue(card) }
            .also(::println)
    }

    override fun second() {
    }
}
