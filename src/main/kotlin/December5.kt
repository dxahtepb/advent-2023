fun main() {
    December5().run()
}

class December5 : Solution() {

    private interface ITransition {
        fun transition(source: Long): Long
    }

    private data class Transition(val sourceStart: Long, val destinationStart: Long, val size: Long) : ITransition {
        private val sourceRange = sourceStart..sourceStart + size

        fun describes(source: Long): Boolean {
            return sourceRange.contains(source)
        }

        override fun transition(source: Long): Long =
            source - sourceStart + destinationStart
    }

    private object DefaultTransition : ITransition {
        override fun transition(source: Long): Long = source
    }

    private fun parseSeeds(rawSeeds: String): List<Long> {
        val (_, rawSeedNumbers) = rawSeeds.split(":")
        return rawSeedNumbers.trim().split(" ").map { it.toLong() }
    }

    private fun parseBlock(rawString: String): List<Transition> {
        return rawString.split("\n").drop(1)
            .map { rawTransition ->
                rawTransition.split(" ")
                    .map { it.toLong() }
                    .let { (destinationStart, sourceStart, size) ->
                        Transition(sourceStart, destinationStart, size)
                    }
            }
    }

    private fun transform(sources: List<Long>, transitions: List<Transition>): List<Long> {
        return sources.map { source ->
            (transitions.find { transition -> transition.describes(source) } ?: DefaultTransition)
                .transition(source)
        }
    }

    override fun first() {
        val text = readFromResources("fifth.txt").use { it.readText() }
        val blocks = text.split("\n\n")
        blocks.drop(1)
            .fold(parseSeeds(blocks.first())) { seeds, block -> transform(seeds, parseBlock(block)) }
            .min()
            .also(::println)
    }

    override fun second() {
    }
}
