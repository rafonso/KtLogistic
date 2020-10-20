package rafael.logistic.bifurcation.henon

import rafael.logistic.map.bifurcation.BifurcationGenerator
import rafael.logistic.map.bifurcation.RData

const val ALPHA_MIN = 0.0
const val ALPHA_MAX = 2.0

const val BETA_MIN = 0.0
const val BETA_MAX = 1.0

const val X_MIN = -1.5
const val X_MAX = +1.5

// https://en.wikipedia.org/wiki/H%C3%A9non_map#One_Dimensional_Decomposition
class HenonBifurcationGenerator : BifurcationGenerator() {

    private var beta = 0.0
    private var xMinus1 = 0.0
    private var xPrior = 0.0

    override fun initValues(sequence: DoubleArray, x0: Double) {
        sequence[0] = xMinus1
        sequence[1] = x0

        xPrior = xMinus1
    }

    override fun initPosSequence(): Int = 2

    override fun getNextX(@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") alpha: Double, x: Double): Double = (1.0 - alpha * x * x + beta * xPrior).also {
        xPrior = x
    }

    fun generate(
        x0: Double,
        rMin: Double,
        rMax: Double,
        stepsForR: Int,
        percentToSkip: Int,
        iterationsPerR: Int,
        beta: Double, xMinus1: Double
    ): List<RData> {
        this.beta = beta
        this.xMinus1 = xMinus1
        this.xPrior = xMinus1

        return super.generate(x0, rMin, rMax, stepsForR, percentToSkip, iterationsPerR)
    }

}
