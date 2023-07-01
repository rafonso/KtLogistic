package rafael.logistic.map.tent

import rafael.logistic.map.generation.IterationGeneratorDouble
import rafael.logistic.core.generation.IterationParameter

data class TentMapParameter(val mi: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Tent_map
class TentMapGenerator : IterationGeneratorDouble<TentMapParameter>() {

    override fun calculate(
        parameter: TentMapParameter,
        x: Double
    ): Double = if (x <= 0.5) parameter.mi * x else parameter.mi * (1 - x)

    fun generate(x0: Double, mi: Double, iterations: Int): List<Double> =
        super.generate(x0, TentMapParameter(mi), iterations)

}
