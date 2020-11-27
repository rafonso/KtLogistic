package rafael.logistic.map.gaussian

import rafael.logistic.map.generation.IterationGeneratorDouble
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.exp

data class GaussianParameter(val alpha: Double, val beta: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Gauss_iterated_map
class GaussianMapGenerator : IterationGeneratorDouble<GaussianParameter>() {

    companion object {
        fun calc(alpha: Double, beta: Double, x: Double): Double = exp(-alpha * x * x) + beta
    }

    override fun calculate(
        parameter: GaussianParameter,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") x: Double
    ): Double = calc(parameter.alpha, parameter.beta, x)

    fun generate(x0: Double, alpha: Double, beta: Double, iterations: Int): List<Double> =
        super.generate(x0, GaussianParameter(alpha, beta), iterations)

}
