package rafael.logistic.maps.bifurcation

import java.time.Duration
import java.time.LocalTime

fun main() {
    val x0Min = 0.000
    val x0Max = 1.000
    val rMin = 3.900
    val rMax = 4.000
    val stepsForR = 2000
    val percentToSkip = 0
    val iterationsPerR = 1000

    val x0Step = 1000
    val deltaX0 = (x0Max - x0Min) / x0Step

    val generator = LogisticBifurcationGenerator()

    // Heating
    repeat(10) { generator.generate(x0Min, rMin, rMax, stepsForR, percentToSkip, iterationsPerR) }

    val t0 = System.currentTimeMillis()

    (0..x0Step).forEach { i ->
        val x0 = x0Min + deltaX0 * i
        print("%.3f".format(x0))

        val t0i = LocalTime.now()
        generator.generate(x0, rMin, rMax, stepsForR, percentToSkip, iterationsPerR)
        val t1i = LocalTime.now()

        println("\t${Duration.between(t0i, t1i).toMillis()}")
    }

    println("-".repeat(60))
    println("TEMPO TOTAL: ${System.currentTimeMillis() - t0}")
}
