package rafael.logistic.maps.gaussian

import rafael.logistic.generator.IteractionGeneratorDouble
import rafael.logistic.generator.IterationParameter
import kotlin.math.exp

data class GaussianParameter(val alpha: Double, val beta: Double) : IterationParameter

class GaussianGenerator : IteractionGeneratorDouble<GaussianParameter>() {

    companion object {
        fun calc(alpha: Double, beta: Double, x: Double): Double = exp(-alpha * x * x) + beta
    }

    override fun calculate(parameter: GaussianParameter, x: Double): Double = calc(parameter.alpha, parameter.beta, x)

    fun generate(x0: Double, alpha: Double, beta: Double, iteractions: Int): List<Double> =
            super.generate(x0, GaussianParameter(alpha, beta), iteractions)

}
