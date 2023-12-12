fun main() {
    December6().run()
}

class December6 : Solution() {

    private data class Race(val time: Long, val distance: Long)

    private fun getRaces(): List<Race> {
        val text = readFromResources("sixth.txt").use { it.readText() }
        val (rawTimes, rawDistances) = text.split('\n')
        val times = rawTimes.split(Regex("\\s+")).drop(1).map { it.toLong() }
        val distances = rawDistances.split(Regex("\\s+")).drop(1).map { it.toLong() }
        return times.zip(distances).map { (time, distance) -> Race(time, distance) }
    }

    private fun getRacesSecond(): List<Race> {
        val text = readFromResources("sixth.txt").use { it.readText() }
        val (time, distance) = text.split('\n')
            .map { it.split(":")[1] }
            .map { it.replace(Regex("\\s+"), "").toLong() }
        return listOf(Race(time, distance))
    }

    private fun countWinConditions(race: Race): Long {
        return (0..race.time).count { pressTime ->
            (race.time - pressTime) * pressTime > race.distance
        }.toLong()
    }

    private fun countProductOfWinConditions(races: List<Race>): Long {
        return races
            .map { countWinConditions(it) }
            .reduce { acc, winConditionsCount -> acc * winConditionsCount }
    }

    override fun first() {
        println(countProductOfWinConditions(getRaces()))
    }

    override fun second() {
        println(countProductOfWinConditions(getRacesSecond()))
    }
}
