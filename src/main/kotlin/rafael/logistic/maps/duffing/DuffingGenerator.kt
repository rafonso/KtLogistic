package rafael.logistic.maps.duffing

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.IterationParameter
import kotlin.math.pow

data class DuffingParameter(val alpha: Double, val beta: Double) : IterationParameter

class DuffingGenerator : IterationGeneratorBi<DuffingParameter>() {

    override fun calculate(parameter: DuffingParameter, point: BiPoint): BiPoint =
            BiPoint(point.y, -parameter.beta * point.x + parameter.alpha * point.y - point.y.pow(3))

    fun generate(p0: BiPoint, alpha: Double, beta: Double, iterations: Int) =
            super.generate(p0, DuffingParameter(alpha, beta), iterations)

}
