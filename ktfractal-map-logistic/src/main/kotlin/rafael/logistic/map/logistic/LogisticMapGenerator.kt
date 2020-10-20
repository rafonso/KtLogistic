package rafael.logistic.map.logistic

import rafael.logistic.core.generation.IterationGeneratorDouble
import rafael.logistic.core.generation.IterationParameter

data class LogisticParameter(val r: Double) : IterationParameter

class LogisticMapGenerator : IterationGeneratorDouble<LogisticParameter>() {

    override fun calculate(
        parameter: LogisticParameter,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") x: Double
    ): Double = x * parameter.r * (1.0 - x)

    fun generate(x0: Double, r: Double, iterations: Int): List<Double> =
        super.generate(x0, LogisticParameter(r), iterations)

}
