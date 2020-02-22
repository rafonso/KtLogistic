package rafael.logistic.maps.henon

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.IterationParameter

data class HenonParameter(val alpha: Double, val beta: Double) : IterationParameter

class HenonGenerator : IteractionGeneratorBi<HenonParameter>() {

    override fun calculate(parameter: HenonParameter, point: BiPoint): BiPoint =
            BiPoint(1.0 - parameter.alpha * point.x * point.x + point.y, parameter.beta * point.x)

    fun generate(p0: BiPoint, alpha: Double, beta: Double, iteractions: Int) =
            super.generate(p0, HenonParameter(alpha, beta), iteractions)

}
