package rafael.logistic.bifurcation.lozi

import rafael.logistic.map.bifurcation.BifurcationGeneratorWithPrior
import rafael.logistic.map.bifurcation.BifurcationParameterWithPrior
import rafael.logistic.map.bifurcation.RData
import kotlin.math.absoluteValue

const val ALPHA_MIN = 0.0
const val ALPHA_MAX = 2.0

const val BETA_MIN = 0.0
const val BETA_MAX = 1.0

const val X_MIN = -2.0
const val X_MAX = +2.0

data class LoziBifurcationParameter(
    override val iterationsPerR: Int,
    override val stepsForR: Int,
    override val rMin: Double,
    override val rMax: Double,
    override val percentToSkip: Int,
    override val xMinus1: Double,
    val beta: Double
) : BifurcationParameterWithPrior(iterationsPerR, stepsForR, rMin, rMax, percentToSkip, xMinus1)

// https://mathworld.wolfram.com/LoziMap.html
class LoziBifurcationGenerator : BifurcationGeneratorWithPrior<LoziBifurcationParameter>() {

    override fun getNextXWithPrior(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") alpha: Double,
        x: Double, xPrior: Double,
        parameter: LoziBifurcationParameter
    ): Double =
        (1.0 - alpha * x.absoluteValue + parameter.beta * xPrior)

    fun generate(
        x0: Double,
        rMin: Double,
        rMax: Double,
        stepsForR: Int,
        percentToSkip: Int,
        iterationsPerR: Int,
        xMinus1: Double, beta: Double
    ): List<RData> =
        super.generate(
            x0,
            LoziBifurcationParameter(iterationsPerR, stepsForR, rMin, rMax, percentToSkip, xMinus1, beta),
            iterationsPerR
        )

}
