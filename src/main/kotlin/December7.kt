fun main() {
    December7().run()
}

class December7 : Solution() {

    companion object {
        private const val CARDS = "AKQT98765432J"
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

    private fun getPriority(hand: String): Int {
        val countCards = hand.groupingBy { it }.eachCount()
        return testers.first { it.tester.test(countCards) }.priority
    }

    private fun getPriorityWithJokers(hand: String): Int {
        val deque = ArrayDeque<String>()
        val knownPrioritiesCache = mutableMapOf<String, Int>()
        var leastPriority = Int.MAX_VALUE
        deque.addLast(hand)
        while (!deque.isEmpty()) {
            val currentHand = deque.removeFirst()
            if (currentHand.contains('J')) {
                for (ch in CARDS.dropLast(1)) {
                    deque.addFirst(currentHand.replaceFirst('J', ch))
                }
            } else {
                val handdPriority = knownPrioritiesCache.computeIfAbsent(currentHand) {
                    val countCards = currentHand.groupingBy { it }.eachCount()
                    testers.first { it.tester.test(countCards) }.priority
                }
                if (handdPriority < leastPriority) leastPriority = handdPriority
            }
        }
        return leastPriority
    }

    private fun tiebreaker(left: Hand, right: Hand): Int {
        return Comparator.comparingInt<List<Int>> { it[0] }
            .thenComparingInt { it[1] }
            .thenComparingInt { it[2] }
            .thenComparingInt { it[3] }
            .thenComparingInt { it[4] }
            .compare(left.hand.map { CARDS.indexOf(it) }, right.hand.map { CARDS.indexOf(it) })
    }

    private fun getCompareHandsFunctionWithPriorityFunction(priorityFunction: (String) -> Int): (Hand, Hand) -> Int {
        return { left, right ->
            val priorityResult = priorityFunction(left.hand).compareTo(priorityFunction(right.hand))
            if (priorityResult != 0) priorityResult
            else tiebreaker(left, right)
        }
    }

    private fun parseHand(rawHandAndBid: String): Hand {
        val (hand, bid) = rawHandAndBid.trim().split(" ")
        return Hand(hand, bid.toInt())
    }

    private fun countTotalWinningsWithRules(lines: List<String>, priorityFunction: (String) -> Int) =
        lines.map { parseHand(it) }
            .sortedWith(getCompareHandsFunctionWithPriorityFunction(priorityFunction))
            .reversed()
            .foldIndexed(0L) { index, acc, hand -> acc + ((index + 1) * hand.bid) }

    override fun first() {
        println(countTotalWinningsWithRules(readLines("seventh.txt"), ::getPriority))
    }

    override fun second() {
        println(countTotalWinningsWithRules(readLines("seventh.txt"), ::getPriorityWithJokers))
    }
}
