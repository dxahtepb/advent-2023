fun main() {
    December3().run()
}

private const val EMPTY = '.'
private const val GEAR = '*'

class December3 : Solution() {

    private class Grid(
        list: List<String>
    ) : ArrayList<String>(list) {
        fun safeGet(position: Position) = getOrNull(position.row)?.getOrNull(position.column) ?: EMPTY
    }

    private data class Number(val row: Int, val columnsRange: IntRange, val value: Int)

    private data class Position(val row: Int, val column: Int) {
        val up get() = Position(row - 1, column)
        val down get() = Position(row + 1, column)
        val right get() = Position(row, column + 1)
        val left get() = Position(row, column - 1)
    }

    private fun parseToGrid(lines: List<String>) = Grid(lines)

    private fun isSymbol(char: Char) = char != EMPTY

    private fun getPositionsToCheck(row: Int, columnsRange: IntRange): List<Position> {
        val leftColumn = columnsRange.first
        val rightColumn = columnsRange.last
        val positionsToCheck = mutableListOf<Position>()
        positionsToCheck.addAll(
            listOf(
                Position(row, leftColumn).up.left,
                Position(row, leftColumn).left,
                Position(row, leftColumn).down.left,
            )
        )
        positionsToCheck.addAll(
            listOf(
                Position(row, rightColumn).up.right,
                Position(row, rightColumn).right,
                Position(row, rightColumn).down.right
            )
        )
        columnsRange
            .forEach {
                positionsToCheck.add(Position(row, it).up)
                positionsToCheck.add(Position(row, it).down)
            }
        return positionsToCheck
    }

    private fun isAdjacentToSymbol(grid: Grid, row: Int, columnsRange: IntRange) =
        getPositionsToCheck(row, columnsRange)
            .any { position -> isSymbol(grid.safeGet(position)) }

    private fun findAllNumbers(grid: Grid) = grid.flatMapIndexed { rowIndex, rowText ->
        Regex("\\d+").findAll(rowText)
            .map { matchResult -> Number(rowIndex, matchResult.range, matchResult.value.toInt()) }
    }

    override fun first() {
        val grid = parseToGrid(readLines("third.txt"))
        findAllNumbers(grid)
            .filter { num -> isAdjacentToSymbol(grid, num.row, num.columnsRange) }
            .sumOf { num -> num.value }
            .also(::println)
    }

    private data class Gear(val ratio: Int, val adjacent: Int) {
        fun mergeWith(otherGear: Gear) = Gear(
            ratio * otherGear.ratio,
            adjacent + otherGear.adjacent
        )
    }

    private fun isGear(char: Char) = char == GEAR

    private fun mergeGears(positionToGear: List<Pair<Position, Gear>>): Map<Position, Gear> {
        val mergedGears = mutableMapOf<Position, Gear>()
        positionToGear.forEach { (position, gear) ->
            mergedGears.merge(position, gear, Gear::mergeWith)
        }
        return mergedGears
    }

    override fun second() {
        val grid = parseToGrid(readLines("third.txt"))
        val positionToGear = findAllNumbers(grid).flatMap { number ->
            getPositionsToCheck(number.row, number.columnsRange)
                .filter { position -> isGear(grid.safeGet(position)) }
                .map { it to Gear(number.value, 1) }
        }
        mergeGears(positionToGear).values
            .filter { gear -> gear.adjacent == 2 }
            .sumOf { gear -> gear.ratio }
            .also(::println)
    }
}
