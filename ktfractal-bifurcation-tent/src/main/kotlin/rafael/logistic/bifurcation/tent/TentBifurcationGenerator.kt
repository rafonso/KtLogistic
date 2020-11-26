package rafael.logistic.bifurcation.tent

import rafael.logistic.bifurcation.BifurcationGenerator
import rafael.logistic.bifurcation.BifurcationParameter
import rafael.logistic.bifurcation.RData

const val MI_MIN = 0.0
const val MI_MAX = 2.0

const val X_MIN = 0.0
const val X_MAX = 1.0

// https://en.wikipedia.org/wiki/Tent_map
class TentBifurcationGenerator : BifurcationGenerator<BifurcationParameter>() {

    override fun getNextX(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") mi: Double,
        x: Double,
        parameter: BifurcationParameter
    ): Double =
        if (x <= 0.5) mi * x else mi * (1 - x)

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
