package rafael.logistic.map.tent

import rafael.logistic.core.generation.IterationGeneratorDouble
import rafael.logistic.core.generation.IterationParameter

data class TentParameter(val mi: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Tent_map
class TentGenerator : IterationGeneratorDouble<TentParameter>() {

    override fun calculate(
        parameter: TentParameter,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") x: Double
    ): Double = if (x <= 0.5) parameter.mi * x else parameter.mi * (1 - x)

    fun generate(x0: Double, mi: Double, iterations: Int): List<Double> =
        super.generate(x0, TentParameter(mi), iterations)

}
