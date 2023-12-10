fun main() {
    December3().run()
}

private const val EMPTY = '.'
private const val GEAR = '*'

class December3 : Solution() {

    private class Grid(
        list: List<String>,
        val rows: Int = list.size,
        val columns: Int = list.getOrNull(0)?.length ?: 0
    ) : ArrayList<String>(list) {
        fun safeGet(row: Int, column: Int) = getOrNull(row)?.getOrNull(column) ?: EMPTY
        fun safeGet(position: Position) = getOrNull(position.row)?.getOrNull(position.column) ?: EMPTY
    }

    private fun parseToGrid(lines: List<String>) = Grid(lines)

    private data class Position(val row: Int, val column: Int) {
        val up get() = Position(row - 1, column)
        val down get() = Position(row + 1, column)
        val right get() = Position(row, column + 1)
        val left get() = Position(row, column - 1)
    }

    private fun isSymbol(char: Char) = char != EMPTY

    private fun getPositionsToCheck(row: Int, columnsRange: IntRange): List<Position> {
        val leftColumn = columnsRange.first
        val rightColumn = columnsRange.last
        val positionsToCheck = mutableListOf<Position>()
        positionsToCheck.addAll(listOf(
            Position(row, leftColumn).up.left,
            Position(row, leftColumn).left,
            Position(row, leftColumn).down.left,
        ))
        positionsToCheck.addAll(listOf(
            Position(row, rightColumn).up.right,
            Position(row, rightColumn).right,
            Position(row, rightColumn).down.right
        ))
        columnsRange
            .forEach {
                positionsToCheck.add(Position(row, it).up)
                positionsToCheck.add(Position(row, it).down)
            }
        return positionsToCheck
    }

    override fun first() {
        val grid = parseToGrid(readLines("third.txt"))
        grid.mapIndexed { row, rowText ->
            Regex("\\d+").findAll(rowText)
                .filter { matchResult ->
                    getPositionsToCheck(row, matchResult.range)
                        .any { position -> isSymbol(grid.safeGet(position)) }
                }.sumOf { matchResult -> matchResult.value.toInt() }
        }.sum().also(::println)
    }

    private data class Gear(val ratio: Int, val adjacent: Int)

    private fun isGear(char: Char) = char == GEAR

    override fun second() {
        val grid = parseToGrid(readLines("third.txt"))
        val knownGears = mutableMapOf<Position, Gear>()
        grid.mapIndexed { row, rowText ->
            Regex("\\d+").findAll(rowText)
                .filter { matchResult ->
                    getPositionsToCheck(row, matchResult.range)
                        .forEach { position ->
                            if ()
                        }
                }.sumOf { matchResult -> matchResult.value.toInt() }
        }.sum().also(::println)
    }
}
