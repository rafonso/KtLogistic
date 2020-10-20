package rafael.logistic.bifurcation.hiperbolic_tangent.data

import rafael.logistic.bifurcation.hiperbolic_tangent.*
import java.time.Duration
import java.time.LocalTime

fun main() {
    val x0Min = X_MIN
    val x0Max = X_MAX
    val rMin = G_MIN
    val rMax = G_MAX
    val stepsForR = 1000
    val percentToSkip = 0
    val iterationsPerR = 1000

    val x0Steps = 300
    val deltaX0 = (x0Max - x0Min) / x0Steps

    val generator = HiperbolicTangentBifurcationGenerator()

    // Heating
    repeat(10) { generator.generate(x0Min, rMin, rMax, stepsForR, percentToSkip, iterationsPerR) }

    val t0 = LocalTime.now()

    (0..x0Steps).forEach { i ->
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
