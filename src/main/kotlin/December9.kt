fun main() {
    December9().run()
}

class December9 : Solution() {

    private fun extrapolateLast(numbers: List<Long>): Long {
        if (numbers.all { it == 0L }) return 0
        return numbers.last() + extrapolateLast(numbers.windowed(2) { (left, right) -> right - left })
    }

    private fun extrapolateFirst(numbers: List<Long>): Long {
        if (numbers.all { it == 0L }) return 0
        return numbers.first() - extrapolateFirst(numbers.windowed(2) { (left, right) -> right - left })
    }

    private fun parseHistory(line: String) = line.split(" ").map { it.toLong() }

    override fun first() {
        readLines("ninth.txt")
            .map { parseHistory(it) }
            .sumOf { extrapolateLast(it) }
            .also { println(it) }
    }

    override fun second() {
        readLines("ninth.txt")
            .map { parseHistory(it) }
            .sumOf { extrapolateFirst(it) }
            .also { println(it) }
    }
}
