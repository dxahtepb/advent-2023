fun main() {
    December2().run()
    December2Parser().run()
}

class December2 : Solution() {

    private data class NumberAndColor(val number: Int, val color: String)

    override fun first() {
        val sizes = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )
        readLines("second.txt").sumOf {
            val line = it.replace(" ", "")
            val (game, balls) = line.split(":")
            val gameNumber = getGameNumber(game)
            if (couldBe(balls, sizes)) gameNumber else 0
        }.also(::println)
    }

    private fun couldBe(balls: String, size: Map<String, Int>): Boolean {
        return getRounds(balls).all { round ->
            getBuckets(round).none { bucket ->
                isBucketPossible(bucket, size)
            }
        }
    }

    private fun isBucketPossible(bucket: String, size: Map<String, Int>): Boolean {
        val (number, color) = getBucketNumberAndColor(bucket)
        return number > size[color]!!
    }

    private fun getBucketNumberAndColor(bucket: String): NumberAndColor {
        val (_, number, color) = Regex("(\\d+)(\\w+)").find(bucket)?.groupValues!!
        return NumberAndColor(number.toInt(), color)
    }

    private fun getBuckets(round: String) = round.split(",")

    private fun getRounds(balls: String): List<String> = balls.split(";")

    private fun getGameNumber(game: String) =
        Regex("\\d+").find(game)?.value?.toInt()!!

    override fun second() {
        readLines("second.txt").sumOf {
            val line = it.replace(" ", "")
            val (_, balls) = line.split(":")
            getSetPower(balls)
        }.also(::println)
    }

    private fun getSetPower(balls: String): Int {
        val possibleSizes = mutableMapOf(
            "red" to 0,
            "green" to 0,
            "blue" to 0,
        )
        getRounds(balls).forEach { round ->
            getBuckets(round).forEach { bucket ->
                val (number, color) = getBucketNumberAndColor(bucket)
                if (possibleSizes[color]!! < number) {
                    possibleSizes[color] = number
                }
            }
        }
        return possibleSizes.values.reduce { acc, it -> acc * it }
    }
}

private sealed class Expression

private data class Game(val number: Int, val round: List<Round>) : Expression()
private data class Round(val draws: List<Draw>) : Expression()
private data class Draw(val number: Int, val color: String) : Expression()

class December2Parser : Solution() {

    // private val grammar = object : Grammar<Expression>() {
        // val ws by regexToken("\\s+", ignore = true)
        //
        // val gameLiteral by literalToken("Game")
        // val red by literalToken("red")
        // val green by literalToken("green")
        // val blue by literalToken("blue")
        //
        // val roundSeparator by literalToken(";")
        // val drawSeparator by literalToken(",")
        //
        // val draw by leftAssociative()
        //
        // val game by (
        //     val gameHeader = gameLiteral use
        //     val roundChain by leftAssociative()
        // )
        // val lpar by literalToken("(")
        // val rpar by literalToken(")")

        // val term by
        // (id use { Variable(text) }) or
        //     (-not * parser(this::term) map { Not(it) }) or
        //     (-lpar * parser(this::rootParser) * -rpar)
        //
        // val andChain by leftAssociative(term, and) { l, _, r -> And(l, r) }
        // override val rootParser by leftAssociative(andChain, or) { l, _, r -> Or(l, r) }
    // }

    override fun first() {
    }

    override fun second() {
    }
}
