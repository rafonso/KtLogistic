package rafael.logistic.map.henon

import rafael.logistic.map.bifurcation.BifurcationGenerator
import rafael.logistic.map.bifurcation.IBifurcationParameter
import rafael.logistic.map.bifurcation.RData

const val ALPHA_MIN = 0.0
const val ALPHA_MAX = 2.0

const val BETA_MIN = 0.0
const val BETA_MAX = 1.0

const val X_MIN = -1.5
const val X_MAX = +1.5

data class HenonBifurcationParameter(
    override val iterationsPerR: Int,
    override val stepsForR: Int,
    override val rMin: Double,
    override val rMax: Double,
    override val percentToSkip: Int,
    val beta: Double,
    val xMinus1: Double
) : IBifurcationParameter {
    var xPrior = xMinus1
}

// https://en.wikipedia.org/wiki/H%C3%A9non_map#One_Dimensional_Decomposition
class HenonBifurcationGenerator : BifurcationGenerator<HenonBifurcationParameter>() {

    override fun initValues(sequence: DoubleArray, x0: Double, parameter: HenonBifurcationParameter) {
        sequence[0] = parameter.xMinus1
        sequence[1] = x0

        parameter.xPrior = parameter.xMinus1
    }

    override fun initPosSequence(): Int = 2

    override fun getNextX(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") alpha: Double,
        x: Double,
        parameter: HenonBifurcationParameter
    ): Double =
        (1.0 - alpha * x * x + parameter.beta * parameter.xPrior).also {
            parameter.xPrior = x
        }

    fun generate(
        x0: Double,
        rMin: Double,
        rMax: Double,
        stepsForR: Int,
        percentToSkip: Int,
        iterationsPerR: Int,
        beta: Double, xMinus1: Double
    ): List<RData> = super.generate(
        x0,
        HenonBifurcationParameter(iterationsPerR, stepsForR, rMin, rMax, percentToSkip, beta, xMinus1),
        iterationsPerR
    )

}
