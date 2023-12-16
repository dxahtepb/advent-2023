import kotlin.reflect.KProperty1

fun main() {
    December10().run()
}

class December10 : Solution() {

    private data class Position(val row: Int, val column: Int) {
        val up get() = Position(row - 1, column)
        val down get() = Position(row + 1, column)
        val right get() = Position(row, column + 1)
        val left get() = Position(row, column - 1)
    }

    private val moves = mapOf(
        '-' to listOf(Position::left, Position::right),
        '|' to listOf(Position::up, Position::down),
        'L' to listOf(Position::up, Position::right),
        'J' to listOf(Position::up, Position::left),
        '7' to listOf(Position::down, Position::left),
        'F' to listOf(Position::down, Position::right),
        'S' to listOf(Position::left, Position::right, Position::up, Position::down),
        '.' to listOf(),
    )

    private fun List<String>.get(position: Position): Char {
        return getOrNull(position.row)?.getOrNull(position.column) ?: '.'
    }

    private fun canConnect(move: KProperty1<Position, Position>, to: Char): Boolean {
        if (to == 'S') return false
        return moves[to]
            ?.any { backMove -> backMove(move(Position(0, 0))) == Position(0, 0) }
            ?: false
    }

    private fun findStart(lines: List<String>): Position {
        lines.forEachIndexed { index, line ->
            val j = line.indexOfFirst { it == 'S' }
            if (j != -1) return Position(index, j)
        }
        throw IllegalStateException("No start")
    }

    private fun countDistances(grid: List<String>, start: Position): Int {
        val deque = ArrayDeque<Pair<Position, Int>>()
        val distances = List(grid.size) { idx -> MutableList(grid[idx].length) { Int.MAX_VALUE } }
        deque.addLast(start to 0)
        while (!deque.isEmpty()) {
            val (currentPosition, currentDistance) = deque.removeFirst()
            if (distances[currentPosition.row][currentPosition.column] < currentDistance) continue
            if (currentDistance == distances[currentPosition.row][currentPosition.column]) {
                return currentDistance
            }
            distances[currentPosition.row][currentPosition.column] = currentDistance
            moves[grid.get(currentPosition)]
                ?.filter { move -> canConnect(move, grid.get(move(currentPosition))) }
                ?.forEach { move -> deque.addLast(move(currentPosition) to currentDistance + 1) }
        }
        throw IllegalStateException()
    }

    override fun first() {
        val lines = readLines("tenth.txt")
        val start = findStart(lines)
        println(countDistances(lines, start))
    }

    override fun second() {
    }
}
