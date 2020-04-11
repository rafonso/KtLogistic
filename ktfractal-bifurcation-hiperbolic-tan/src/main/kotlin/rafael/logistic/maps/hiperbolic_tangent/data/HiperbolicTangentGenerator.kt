package rafael.logistic.maps.hiperbolic_tangent.data

import rafael.logistic.maps.bifurcation.BifurcationGenerator
import java.time.Duration
import java.time.LocalTime
import kotlin.math.tanh

const val G_MIN = 0.0
const val G_MAX = 20.0
const val X_MIN = 0.0
const val X_MAX = 6.0

// https://en.wikipedia.org/wiki/Logistic_map#Feigenbaum_universality_of_1-D_maps
class HiperbolicTangentGenerator : BifurcationGenerator() {

    override fun getNextX(r: Double, x: Double): Double = r * x * (1.0 - tanh(x))

}

fun main() {
    val x0Min = X_MIN
    val x0Max = X_MAX
    val rMin = G_MIN
    val rMax = G_MAX
    val stepsForR = 1000
    val percentToSkip = 0
    val iterationsPerR = 1000

    val x0Step = 1000
    val deltaX0 = (x0Max - x0Min) / x0Step

    val generator = HiperbolicTangentGenerator()

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
