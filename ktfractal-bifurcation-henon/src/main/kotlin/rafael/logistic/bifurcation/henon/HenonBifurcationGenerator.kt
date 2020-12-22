package rafael.logistic.bifurcation.henon

import rafael.logistic.bifurcation.BifurcationGeneratorWithPrior
import rafael.logistic.bifurcation.BifurcationParameterWithPrior
import rafael.logistic.bifurcation.RData

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
    override val firstIteration: Int,
    override val xMinus1: Double,
    val beta: Double
) : BifurcationParameterWithPrior(iterationsPerR, stepsForR, rMin, rMax, firstIteration, xMinus1)

// https://en.wikipedia.org/wiki/H%C3%A9non_map#One_Dimensional_Decomposition
class HenonBifurcationGenerator : BifurcationGeneratorWithPrior<HenonBifurcationParameter>() {

    override fun getNextXWithPrior(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") alpha: Double,
        x: Double, xPrior: Double,
        parameter: HenonBifurcationParameter
    ): Double =
        (1.0 - alpha * x * x + parameter.beta * xPrior)

    fun generate(
        x0: Double,
        rMin: Double,
        rMax: Double,
        stepsForR: Int,
        firstIteration: Int,
        iterationsPerR: Int,
        xMinus1: Double,
        beta: Double
    ): List<RData> = super.generate(
        x0,
        HenonBifurcationParameter(iterationsPerR, stepsForR, rMin, rMax, firstIteration, xMinus1, beta),
        iterationsPerR
    )

}
