fun main() {
    December7().run()
}

class December7 : Solution() {

    companion object {
        private const val CARDS = "AKQJT98765432"
    }

    private data class Hand(val hand: String, val bid: Int)
    private data class Tester(val name: String, val test: (Map<Char, Int>) -> Boolean)

    private val testers = listOf(
        Tester("Five of a kind") { countCards -> countCards.size == 1 },
        Tester("Four of a kind") { countCards -> countCards.values.sorted() == listOf(1, 4) },
        Tester("Full house") { countCards -> countCards.values.sorted() == listOf(2, 3) },
        Tester("Three of a kind") { countCards -> countCards.values.sorted() == listOf(1, 1, 3) },
        Tester("Two pair") { countCards -> countCards.values.sorted() == listOf(1, 2, 2) },
        Tester("One pair") { countCards -> countCards.values.sorted() == listOf(1, 1, 1, 2) },
        Tester("High card") { _ -> true },
    ).mapIndexed { index, tester -> index to tester }

    private fun getPriority(hand: String): Int {
        val countCards = hand.groupingBy { it }.eachCount()
        return testers.first { (_, tester) -> tester.test.invoke(countCards) }.first
    }

    private fun tiebreaker(left: Hand, right: Hand): Int {
        return Comparator.comparingInt<List<Int>> { it[0] }
            .thenComparingInt { it[1] }
            .thenComparingInt { it[2] }
            .thenComparingInt { it[3] }
            .thenComparingInt { it[4] }
            .compare(left.hand.map { CARDS.indexOf(it) }, right.hand.map { CARDS.indexOf(it) })
    }

    private fun compareHands(left: Hand, right: Hand): Int {
        val priorityResult = getPriority(left.hand).compareTo(getPriority(right.hand))
        return if (priorityResult != 0) priorityResult
        else tiebreaker(left, right)
    }

    private fun parseHand(rawHandAndBid: String): Hand {
        val (hand, bid) = rawHandAndBid.trim().split(" ")
        return Hand(hand, bid.toInt())
    }

    override fun first() {
        readLines("seventh.txt")
            .map { parseHand(it) }
            .sortedWith(::compareHands)
            .reversed()
            .foldIndexed(0L) { index, acc, hand -> acc + ((index + 1) * hand.bid) }
            .also { println(it) }
    }

    override fun second() {
    }
}
