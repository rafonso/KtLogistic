package rafael.logistic.bifurcation.logistic

import rafael.logistic.map.bifurcation.BifurcationGenerator
import rafael.logistic.map.bifurcation.BifurcationParameter
import rafael.logistic.map.bifurcation.RData

const val R_MIN = 0.0
const val R_MAX = 4.0
const val X_MIN = 0.0
const val X_MAX = 1.0

class LogisticBifurcationGenerator : BifurcationGenerator<BifurcationParameter>() {

    override fun getNextX(r: Double, x: Double, parameter: BifurcationParameter): Double =
        r * x * (1.0 - x)

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
