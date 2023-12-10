import java.io.BufferedReader
import java.io.InputStream

fun readFromResources(filename: String): BufferedReader {
    val inputStream: InputStream = object {}::class.java.classLoader.getResourceAsStream(filename)
        ?: throw IllegalArgumentException("File not found: $filename")

    return inputStream.bufferedReader()
}

fun readLines(filename: String): List<String> {
    return readFromResources(filename).use { it.lines().toList() }
}
