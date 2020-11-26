package rafael.logistic.bifurcation.gaussian

import rafael.logistic.bifurcation.BifurcationGenerator
import rafael.logistic.bifurcation.IBifurcationParameter
import rafael.logistic.bifurcation.RData
import kotlin.math.exp

const val ALPHA_MIN = 00.0
const val ALPHA_MAX = 10.0

const val BETA_MIN = -1.0
const val BETA_MAX = +1.0

const val X_MIN = -1.0
const val X_MAX = +1.5

data class GaussianBifurcationParameter(
    override val iterationsPerR: Int,
    override val stepsForR: Int,
    override val rMin: Double,
    override val rMax: Double,
    override val percentToSkip: Int,
             val alpha: Double
) : IBifurcationParameter


// https://en.wikipedia.org/wiki/Gauss_iterated_map
class GaussianBifurcationGenerator : BifurcationGenerator<GaussianBifurcationParameter>() {

    override fun getNextX(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") beta: Double,
        x: Double,
        parameter: GaussianBifurcationParameter
    ): Double =
        exp(-parameter.alpha * x * x) + beta

    fun generate(
        x0: Double,
        rMin: Double,
        rMax: Double,
        stepsForR: Int,
        percentToSkip: Int,
        iterationsPerR: Int,
        alpha: Double
    ): List<RData> =
        super.generate(
            x0,
            GaussianBifurcationParameter(iterationsPerR, stepsForR, rMin, rMax, percentToSkip, alpha),
            iterationsPerR
        )

}
