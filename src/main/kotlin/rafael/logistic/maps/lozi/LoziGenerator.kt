package rafael.logistic.maps.lozi

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.IterationParameter
import kotlin.math.absoluteValue

data class LoziParameter(val alpha: Double, val beta: Double) : IterationParameter

class LoziGenerator : IteractionGeneratorBi<LoziParameter>() {

    override fun calculate(parameter: LoziParameter, point: BiPoint): BiPoint =
            BiPoint(1.0 - parameter.alpha * point.x.absoluteValue + point.y, parameter.beta * point.x)

    fun generate(p0: BiPoint, alpha: Double, beta: Double, iteractions: Int) =
            super.generate(p0, LoziParameter(alpha, beta), iteractions)

}
