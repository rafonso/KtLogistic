package rafael.logistic.maps.logistic

import rafael.logistic.generator.IterationGeneratorDouble
import rafael.logistic.generator.IterationParameter

data class LogisticParameter(val r: Double) : IterationParameter

class LogisticGenerator : IterationGeneratorDouble<LogisticParameter>() {

    override fun calculate(parameter: LogisticParameter, x: Double): Double = x * parameter.r * (1.0 - x)

    fun generate(x0: Double, r: Double, iterations: Int): List<Double> = super.generate(x0, LogisticParameter(r), iterations)

}
