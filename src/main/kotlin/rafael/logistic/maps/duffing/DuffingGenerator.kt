package rafael.logistic.maps.duffing

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.IteractionParameter
import kotlin.math.pow

data class DuffingParameter(val alpha: Double, val beta: Double) : IteractionParameter

class DuffingGenerator : IteractionGeneratorBi<DuffingParameter>() {

    override fun calculate(parameter: DuffingParameter, point: BiPoint): BiPoint =
            BiPoint(point.y, -parameter.beta * point.x + parameter.alpha * point.y - point.y.pow(3))

    fun generate(p0: BiPoint, alpha: Double, beta: Double, iteractions: Int) =
            super.generate(p0, DuffingParameter(alpha, beta), iteractions)

}
