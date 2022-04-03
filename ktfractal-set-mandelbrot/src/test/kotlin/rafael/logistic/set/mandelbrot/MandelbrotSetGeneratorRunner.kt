package rafael.logistic.set.mandelbrot

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.set.SetParameter
import java.time.Duration
import java.time.LocalTime

fun main() {
    val xMin = -0.25
    val xMax = -0.00
    val width = 600
    val yMin = -0.90
    val yMax = - 0.65
    val iterationsPerDot = 500

    val times = 1000

    val parameter = SetParameter(
        Double.NaN,
        Double.NaN,
        xMin,
        xMax,
        width,
        yMin,
        yMax,
        width
    )

    val generator = MandelbrotSetGenerator()

    // Heating
    (0..10).forEach { _ -> generator.generate(BiDouble.NAN, parameter, iterationsPerDot) }

    val t0 = LocalTime.now()

    val deltas = (1..times).map {
        val t0i = LocalTime.now()
        generator.generate(BiDouble.NAN, parameter, iterationsPerDot)
        val t1i = LocalTime.now()

        Duration.between(t0i, t1i).toMillis() //.also { deltaT -> println("%4d".format(deltaT)) }
    }.toLongArray()


    val t1 = LocalTime.now()
    println("-".repeat(60))
    println("MÉDIA      : %4.2f".format(deltas.average()))
    println("TEMPO TOTAL: ${Duration.between(t0, t1)}")


}
