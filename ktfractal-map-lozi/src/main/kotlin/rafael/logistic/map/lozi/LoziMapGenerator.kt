package rafael.logistic.map.lozi

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.absoluteValue

data class LoziMapParameter(val alpha: Double, val beta: Double) : IterationParameter

// https://mathworld.wolfram.com/LoziMap.html
class LoziMapGenerator : IterationGeneratorBi<LoziMapParameter>() {

    override fun calculate(
        parameter: LoziMapParameter,
        point: BiDouble
    ): BiDouble =
        BiDouble(1.0 - parameter.alpha * point.x.absoluteValue + point.y, parameter.beta * point.x)

    fun generate(p0: BiDouble, alpha: Double, beta: Double, iterations: Int) =
        super.generate(p0, LoziMapParameter(alpha, beta), iterations)

}
