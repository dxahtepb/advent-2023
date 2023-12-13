fun main() {
    December7().run()
}

class December7 : Solution() {

    companion object {
        private const val CARDS = "AKQJT98765432J"
    }

    private data class Hand(val hand: String, val bid: Int)
    private data class Tester(val name: String, private val test: (Map<Char, Int>) -> Boolean) {
        fun test(value: Map<Char, Int>) = test.invoke(value)
    }
    private data class TesterWithPriority(val tester: Tester, val priority: Int)

    private val testers = listOf(
        Tester("Five of a kind") { countCards -> countCards.size == 1 },
        Tester("Four of a kind") { countCards -> countCards.values.sorted() == listOf(1, 4) },
        Tester("Full house") { countCards -> countCards.values.sorted() == listOf(2, 3) },
        Tester("Three of a kind") { countCards -> countCards.values.sorted() == listOf(1, 1, 3) },
        Tester("Two pair") { countCards -> countCards.values.sorted() == listOf(1, 2, 2) },
        Tester("One pair") { countCards -> countCards.values.sorted() == listOf(1, 1, 1, 2) },
        Tester("High card") { _ -> true },
    ).mapIndexed { index, tester -> TesterWithPriority(tester, index) }

    private val testersWithJokers = listOf(
        Tester("Five of a kind") { countCards -> countCards.size == 1 },
        Tester("Four of a kind") { countCards -> countCards.values.sorted() == listOf(1, 4) },
        Tester("Full house") { countCards -> countCards.values.sorted() == listOf(2, 3) },
        Tester("Three of a kind") { countCards -> countCards.values.sorted() == listOf(1, 1, 3) },
        Tester("Two pair") { countCards -> countCards.values.sorted() == listOf(1, 2, 2) },
        Tester("One pair") { countCards -> countCards.values.sorted() == listOf(1, 1, 1, 2) },
        Tester("High card") { _ -> true },
    ).mapIndexed { index, tester -> TesterWithPriority(tester, index) }

    private fun getPriority(testerList: List<TesterWithPriority>, hand: String): Int {
        val countCards = hand.groupingBy { it }.eachCount()
        return testerList.first { it.tester.test(countCards) }.priority
    }

    private fun tiebreaker(left: Hand, right: Hand): Int {
        return Comparator.comparingInt<List<Int>> { it[0] }
            .thenComparingInt { it[1] }
            .thenComparingInt { it[2] }
            .thenComparingInt { it[3] }
            .thenComparingInt { it[4] }
            .compare(left.hand.map { CARDS.indexOf(it) }, right.hand.map { CARDS.indexOf(it) })
    }

    private fun getCompareHandsFunctionWithTesters(testerList: List<TesterWithPriority>): (Hand, Hand) -> Int {
        return { left, right ->
            val priorityResult = getPriority(testerList, left.hand).compareTo(getPriority(testerList, right.hand))
            if (priorityResult != 0) priorityResult
            else tiebreaker(left, right)
        }
    }

    private fun parseHand(rawHandAndBid: String): Hand {
        val (hand, bid) = rawHandAndBid.trim().split(" ")
        return Hand(hand, bid.toInt())
    }

    private fun countTotalWinningsWithRules(lines: List<String>, testerList: List<TesterWithPriority>) {
        lines.map { parseHand(it) }
            .sortedWith(getCompareHandsFunctionWithTesters(testerList))
            .reversed()
            .foldIndexed(0L) { index, acc, hand -> acc + ((index + 1) * hand.bid) }
            .also { println(it) }
    }

    override fun first() {
        println(countTotalWinningsWithRules(readLines("seventh.txt"), testers))
    }

    override fun second() {
        println(countTotalWinningsWithRules(readLines("seventh.txt"), testersWithJokers))
    }
}
