package rafael.logistic.maps.lozi

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.absoluteValue

data class LoziParameter(val alpha: Double, val beta: Double) : IterationParameter

class LoziGenerator : IterationGeneratorBi<LoziParameter>() {

    override fun calculate(parameter: LoziParameter, point: BiDouble): BiDouble =
            BiDouble(1.0 - parameter.alpha * point.x.absoluteValue + point.y, parameter.beta * point.x)

    fun generate(p0: BiDouble, alpha: Double, beta: Double, iterations: Int) =
            super.generate(p0, LoziParameter(alpha, beta), iterations)

}
