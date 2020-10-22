package rafael.logistic.bifurcation.ikeda

import rafael.logistic.map.bifurcation.BifurcationGenerator
import rafael.logistic.map.bifurcation.IBifurcationParameter
import rafael.logistic.map.bifurcation.RData
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

const val U_MIN = -1.0
const val U_MAX = +1.0

const val X_MIN = -5.0
const val X_MAX = +5.0

data class IkedaBifurcationParameter(
    override val iterationsPerR: Int,
    override val stepsForR: Int,
    override val rMin: Double,
    override val rMax: Double,
    override val percentToSkip: Int,
    val xMinus1: Double
) : IBifurcationParameter {
    var xPrior = xMinus1
}

// https://en.wikipedia.org/wiki/Ikeda_map
class IkedaBifurcationGenerator : BifurcationGenerator<IkedaBifurcationParameter>() {

    override fun initValues(sequence: DoubleArray, x0: Double, parameter: IkedaBifurcationParameter) {
        sequence[0] = parameter.xMinus1
        sequence[1] = x0

        parameter.xPrior = parameter.xMinus1
    }

    override fun initPosSequence(): Int = 2

    override fun getNextX(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") u: Double,
        x: Double,
        parameter: IkedaBifurcationParameter
    ): Double {
        val t = 0.4 - 6.0 / (1 + x.pow(2) +  parameter.xPrior.pow(2))

        return (1.0 + u * (x * cos(t) - parameter.xPrior * sin(t)) +
                u * (x * sin(t) + parameter.xPrior * cos(t))).also {
            parameter.xPrior = x
        }
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
