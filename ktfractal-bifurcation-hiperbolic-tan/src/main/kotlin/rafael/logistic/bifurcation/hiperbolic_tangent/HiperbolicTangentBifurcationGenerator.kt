package rafael.logistic.bifurcation.hiperbolic_tangent

import rafael.logistic.bifurcation.BifurcationGenerator
import rafael.logistic.bifurcation.BifurcationParameter
import rafael.logistic.bifurcation.RData
import kotlin.math.tanh

const val G_MIN = 0.0
const val G_MAX = 20.0

const val X_MIN = 0.0
const val X_MAX = 6.0

// https://en.wikipedia.org/wiki/Logistic_map#Feigenbaum_universality_of_1-D_maps
class HiperbolicTangentBifurcationGenerator : BifurcationGenerator<BifurcationParameter>() {

    override fun getNextX(r: Double, x: Double, parameter: BifurcationParameter): Double =
        r * x * (1.0 - tanh(x))

    fun generate(
        x0: Double,
        rMin: Double,
        rMax: Double,
        stepsForR: Int,
        percentToSkip: Int,
        iterationsPerR: Int
    ): List<RData> =
        super.generate(
            x0,
            BifurcationParameter(iterationsPerR, stepsForR, rMin, rMax, percentToSkip),
            iterationsPerR
        )

}
