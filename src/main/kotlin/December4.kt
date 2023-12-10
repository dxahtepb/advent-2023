import kotlin.math.pow

fun main() {
    December4().run()
}

class December4 : Solution() {

    private data class Card(val number: Int, val myNumbers: Set<Int>, val winNumbers: Set<Int>)
    private data class ValuedCard(val number: Int, val value: Int)

    private fun getCardParts(rawCard: String) = rawCard.split(":", "|").map(String::trim)

    private fun getNumberFromHeader(header: String) = header.split(Regex("\\s+"))[1].toInt()

    private fun parseNumbers(myNumbersRaw: String) = myNumbersRaw.split(Regex("\\s+")).map(String::toInt).toSet()

    private fun parseCard(rawCard: String): Card {
        val (header, winNumbersRaw, myNumbersRaw) = getCardParts(rawCard)
        val number = getNumberFromHeader(header)
        val myNumbers = parseNumbers(myNumbersRaw)
        val winNumbers = parseNumbers(winNumbersRaw)
        return Card(number, myNumbers, winNumbers)
    }

    private fun countMatchingNumbers(card: Card) = card.myNumbers.intersect(card.winNumbers).size

    private fun valueCard(card: Card, cardEvaluator: (Card) -> Int): ValuedCard {
        return ValuedCard(card.number, cardEvaluator.invoke(card))
    }

    private val firstPuzzleCardEvaluator = { card: Card ->
        val matchingNumbers = countMatchingNumbers(card)
        if (matchingNumbers == 0) {
            0
        } else {
            2.0.pow(matchingNumbers - 1).toInt()
        }
    }

    private val secondPuzzleCardEvaluator = { card: Card -> countMatchingNumbers(card) }

    override fun first() {
        readLines("fourth.txt")
            .map(this::parseCard)
            .map { valueCard(it, firstPuzzleCardEvaluator) }
            .sumOf(ValuedCard::value)
            .also(::println)
    }

    override fun second() {
        val cardByNumber = readLines("fourth.txt")
            .map(this::parseCard)
            .map { valueCard(it, secondPuzzleCardEvaluator) }
            .associateBy { it.number }
        val deck = ArrayDeque(cardByNumber.values)
        var countOfCards = 0
        while (!deck.isEmpty()) {
            val card = deck.removeFirst()
                .also { countOfCards += 1 }
            for (copyNumber in card.number + 1..card.number + card.value) {
                deck.addLast(cardByNumber[copyNumber]!!)
            }
        }
        println(countOfCards)
    }
}
