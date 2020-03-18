import javafx.geometry.Point2D
import java.time.Duration
import java.time.LocalTime
import kotlin.math.pow

fun testDistance(size: Int, method: (Point2D, Point2D) -> Double): Pair<Int, Long> {
    val t0 = LocalTime.now()

    (1..size).map {
        val p1 = Point2D(Math.random(), Math.random())
        val p2 = Point2D(Math.random(), Math.random())

        method(p1, p2)
    }

    return Pair(size, Duration.between(t0, LocalTime.now()).toMillis())
}

fun distance(p1: Point2D, p2: Point2D) = p1.distance(p2)

fun pow2(p1: Point2D, p2: Point2D) = (p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2)

fun square(p1: Point2D, p2: Point2D) = (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)


fun main(args: Array<String>) {
    val function = ::square

    val cycles = listOf(1, 1_000, 3_000, 10_000, 30_000, 100_000, 300_000, 500_000, 750_000, 1_000_000, 1_500_000, 2_000_000)
    val total = (1..5)

    // Heating
    total.forEach { times -> testDistance(times, function) }

    cycles.forEach { times ->
        total
                .map { testDistance(times, function) }
                .forEach { println("%8d\t%5d".format(it.first, it.second)) }
    }

}