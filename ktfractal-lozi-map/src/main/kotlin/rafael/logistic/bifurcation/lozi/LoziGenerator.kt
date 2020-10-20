package rafael.logistic.bifurcation.lozi

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.absoluteValue

data class LoziParameter(val alpha: Double, val beta: Double) : IterationParameter

// https://mathworld.wolfram.com/LoziMap.html
class LoziGenerator : IterationGeneratorBi<LoziParameter>() {

    override fun calculate(
        parameter: LoziParameter,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") point: BiDouble
    ): BiDouble =
        BiDouble(1.0 - parameter.alpha * point.x.absoluteValue + point.y, parameter.beta * point.x)

    fun generate(p0: BiDouble, alpha: Double, beta: Double, iterations: Int) =
        super.generate(p0, LoziParameter(alpha, beta), iterations)

}
