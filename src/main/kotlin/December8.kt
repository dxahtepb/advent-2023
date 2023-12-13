fun main() {
    December8().run()
}

class December8 : Solution() {

    enum class Direction { LEFT, RIGHT }
    data class Node(val name: String, val left: String, val right: String)
    data class Graph(val nodes: Map<String, Node>)
    data class Route(val directions: List<Direction>) {
        fun get(position: Long) = directions[(position % directions.size.toLong()).toInt()]
    }

    private fun lcm(a: Long, b: Long): Long {
        val gcd = gcd(a, b)
        return (a * b) / gcd
    }

    private tailrec fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    private fun parseLineToNode(line: String): Node {
        val match = Regex("(\\w+) = \\((\\w+), (\\w+)\\)")
            .find(line) ?: throw IllegalArgumentException()
        val (name, left, right) = match.destructured
        return Node(name, left, right)
    }

    private fun getPatternAndGraph(lines: List<String>): Pair<Route, Graph> {
        val pattern = lines[0]
            .map { if (it == 'L') Direction.LEFT else Direction.RIGHT }
        val nodes = lines.drop(2)
            .map { parseLineToNode(it) }
            .associateBy { it.name }
        return Route(pattern) to Graph(nodes)
    }

    private val nameEqualsZZZ = { node: Node -> node.name == "ZZZ" }
    private val nameEndsOnZ = { node: Node -> node.name.endsWith("Z") }

    private fun countSteps(start: Node, route: Route, graph: Graph, isFinish: (Node) -> Boolean): Long {
        var currentNode = start
        var counter = 0L
        while (!isFinish(currentNode)) {
            currentNode = when (route.get(counter)) {
                Direction.LEFT -> graph.nodes[currentNode.left]!!
                Direction.RIGHT -> graph.nodes[currentNode.right]!!
            }
            counter += 1
        }
        return counter
    }

    override fun first() {
        val (route, graph) = getPatternAndGraph(readLines("eighth.txt"))
        println(countSteps(graph.nodes["AAA"]!!, route, graph, nameEqualsZZZ))
    }

    override fun second() {
        val (route, graph) = getPatternAndGraph(readLines("eighth.txt"))
        graph.nodes.values
            .filter { it.name.endsWith("A") }
            .map { start -> countSteps(start, route, graph, nameEndsOnZ) }
            .reduce { acc, curr -> lcm(acc, curr) }
            .also { println(it) }
    }
}
