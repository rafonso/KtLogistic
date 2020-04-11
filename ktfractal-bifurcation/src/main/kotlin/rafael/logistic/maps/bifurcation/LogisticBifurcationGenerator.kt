package rafael.logistic.maps.bifurcation

import java.time.Duration
import java.time.LocalTime

const val R_MIN = 0.0
const val R_MAX = 4.0
const val X_MIN = 0.0
const val X_MAX = 1.0

class LogisticBifurcationGenerator : BifurcationGenerator() {

    override fun getNextX(r: Double, x: Double): Double = r * x * (1.0 - x)

}

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
    (0..10).forEach { generator.generate(x0Min, rMin, rMax, stepsForR, percentToSkip, iterationsPerR) }

    val t0 = LocalTime.now()

    (0..x0Step).forEach { i ->
        val x0 = x0Min + deltaX0 * i
        print("%.3f".format(x0))

        val t0i = LocalTime.now()
        generator.generate(x0, rMin, rMax, stepsForR, percentToSkip, iterationsPerR)
        val t1i = LocalTime.now()

        println("\t${Duration.between(t0i, t1i).toMillis()}")
    }

    val t1 = LocalTime.now()
    println("-".repeat(60))
    println("TEMPO TOTAL: ${Duration.between(t0, t1)}")
}
