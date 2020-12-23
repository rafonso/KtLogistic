package rafael.logistic.bifurcation.logistic

import rafael.logistic.bifurcation.BifurcationGenerator
import rafael.logistic.bifurcation.BifurcationParameter
import rafael.logistic.bifurcation.RData

const val R_MIN = -2.0
const val R_MAX = +4.0
const val X_MIN = -0.5
const val X_MAX = +1.5

class LogisticBifurcationGenerator : BifurcationGenerator<BifurcationParameter>() {

    override fun getNextX(r: Double, x: Double, parameter: BifurcationParameter): Double =
        r * x * (1.0 - x)

    fun generate(
        x0: Double,
        rMin: Double,
        rMax: Double,
        stepsForR: Int,
        firstIteration: Int,
        iterationsPerR: Int
    ): List<RData> =
        super.generate(
            x0,
            BifurcationParameter(iterationsPerR, stepsForR, rMin, rMax, firstIteration),
            iterationsPerR
        )

}
