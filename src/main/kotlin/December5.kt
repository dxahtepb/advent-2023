import kotlin.math.tan

fun main() {
    testCut()
    December5().run()
}

class December5 : Solution() {

    private fun parseSeeds(rawSeeds: String): List<Long> {
        val (_, rawSeedNumbers) = rawSeeds.split(":")
        return rawSeedNumbers.trim().split(" ").map { it.toLong() }
    }

    private fun parseSeedsToRanges(rawSeeds: String): List<LongRange> {
        return parseSeeds(rawSeeds)
            .chunked(2) { (start, size) -> start..<start + size }
    }

    private fun parseBlock(rawString: String): List<Transition> {
        return rawString.split("\n").drop(1)
            .map { rawTransition ->
                rawTransition.split(" ")
                    .map { it.toLong() }
                    .let { (destinationStart, sourceStart, size) ->
                        Transition(sourceStart, destinationStart, size)
                    }
            }.also { assertTransitions(it) }
    }

    private fun assertTransitions(transitions: List<Transition>) {
        transitions.sortedWith(Transition.COMPARATOR)
            .windowed(2) {
                assert(it[0].sourceRange.last <= it[1].sourceRange.first)
            }
    }

    private fun transform(sources: List<Long>, transitions: List<Transition>): List<Long> {
        return sources.map { source ->
            (transitions.find { transition -> transition.describes(source) } ?: DefaultTransition)
                .transition(source)
        }
    }

    private fun transformRanges(sources: List<LongRange>, transitions: List<Transition>): List<LongRange> {
        val sortedTransitions = transitions.sortedWith(Transition.COMPARATOR)
        return sources.flatMap { source ->
            var currentSource = source
            sortedTransitions.flatMap { transition ->
                val (restOfSource, transformedRanges) = cut(currentSource, transition)
                currentSource = restOfSource
                transformedRanges
            }
        }.filterNot { it.isEmpty() }
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
        val text = readFromResources("fifth.txt").use { it.readText() }
        val blocks = text.split("\n\n")
        blocks.drop(1)
            .fold(parseSeedsToRanges(blocks.first())) { seeds, block ->
                transformRanges(
                    seeds,
                    parseBlock(block)
                )
            }
            .minOfOrNull { it.first }
            .also(::println)
    }
}

private interface ITransition {
    fun transition(source: Long): Long
}

private object DefaultTransition : ITransition {
    override fun transition(source: Long): Long = source
}

private data class Transition(val sourceStart: Long, val destinationStart: Long, val size: Long) : ITransition {
    val sourceRange = sourceStart..<(sourceStart + size)
    val destinationRange = destinationStart..<(destinationStart + size)

    fun describes(source: Long): Boolean {
        return sourceRange.contains(source)
    }

    override fun transition(source: Long): Long =
        source - sourceStart + destinationStart

    companion object {
        val COMPARATOR: java.util.Comparator<Transition> =
            Comparator.comparingLong<Transition> { it.sourceRange.first }
                .thenComparingLong { it.sourceRange.last }
    }
}

private fun isRangeIntersect(one: LongRange, two: LongRange) =
    !(one.last < two.first || two.last < one.first)

private fun cut(source: LongRange, transform: Transition): Pair<LongRange, List<LongRange>> {
    val transformSource = transform.sourceRange

    // does not intersect
    if (source.isEmpty() || !isRangeIntersect(source, transformSource)) {
        return source to listOf()
    }

    // transform contains
    if (transformSource.first < source.first && source.last < transformSource.last) {
        return LongRange.EMPTY to listOf(
            (transform.destinationRange.first + source.first - transform.sourceStart)
                ..(transform.destinationRange.first - transform.sourceStart + source.last)
        )
    }

    // left intersection
    if (transformSource.first < source.first) {
        return (transformSource.last + 1)..source.last to listOf(
            (transform.destinationRange.first + source.first - transform.sourceRange.first)
                ..transform.destinationRange.last
        )
    }

    // transform inner
    if (transformSource.last <= source.last) {
        return (transformSource.last + 1)..source.last to listOf(
            source.first..<transformSource.first,
            transform.destinationRange
        )
    }

    // right intersection
    return source.first..<transformSource.first to listOf(
        transform.destinationRange.first..(transform.destinationRange.first + source.last - transform.sourceRange.first)
    )
}

private fun testCut() {
    listOf(
        (1L..10L to Transition(3, 100, 3))
            to (6L..10L to listOf(1L..2L, 100L..102L)),

        (1L..10L to Transition(1, 100, 10))
            to (LongRange.EMPTY to listOf(LongRange.EMPTY, 100L..109L)),

        (2L..7L to Transition(1, 100, 8))
            to (LongRange.EMPTY to listOf(101L..106L)),

        (2L..7L to Transition(0, 100, 9))
            to (LongRange.EMPTY to listOf(102L..107L)),

        (2L..7L to Transition(0, 100, 5))
            to (5L..7L to listOf(102L..104L)),

        (2L..7L to Transition(0, 100, 8))
            to (LongRange.EMPTY to listOf(102L..107L)),

        (2L..7L to Transition(5, 100, 5))
            to (2L..4L to listOf(100L..102L)),

        (2L..7L to Transition(2, 100, 10))
            to (LongRange.EMPTY to listOf(100L..105L)),
    ).forEach { (given, expected) ->
        val (source, transition) = given
        val actual = cut(source, transition)
        assert(actual == expected) { "failed at $given -> $expected, got $actual" }
    }
}
