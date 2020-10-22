package rafael.logistic.bifurcation.ikeda

import rafael.logistic.map.bifurcation.BifurcationGeneratorWithPrior
import rafael.logistic.map.bifurcation.BifurcationParameterWithPrior
import rafael.logistic.map.bifurcation.RData
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

const val U_MIN = -1.0
const val U_MAX = -U_MIN

const val X_MIN = -6.0
const val X_MAX = -X_MIN

data class IkedaBifurcationParameter(
    override val iterationsPerR: Int,
    override val stepsForR: Int,
    override val rMin: Double,
    override val rMax: Double,
    override val percentToSkip: Int,
    override val xMinus1: Double
) : BifurcationParameterWithPrior(iterationsPerR, stepsForR, rMin, rMax, percentToSkip, xMinus1)

// https://en.wikipedia.org/wiki/Ikeda_map
class IkedaBifurcationGenerator : BifurcationGeneratorWithPrior<IkedaBifurcationParameter>() {

    override fun getNextXWithPrior(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") u: Double,
        x: Double, xPrior: Double,
        parameter: IkedaBifurcationParameter
    ): Double {
        val t = 0.4 - 6.0 / (1 + x.pow(2) + xPrior.pow(2))

        return (1.0 + u * (x * cos(t) - xPrior * sin(t)) +
                u * (x * sin(t) + xPrior * cos(t)))
    }

    fun generate(
        x0: Double,
        rMin: Double,
        rMax: Double,
        stepsForR: Int,
        percentToSkip: Int,
        iterationsPerR: Int,
        xMinus1: Double
    ): List<RData> =
        super.generate(
            x0,
            IkedaBifurcationParameter(iterationsPerR, stepsForR, rMin, rMax, percentToSkip, xMinus1),
            iterationsPerR
        )

}
