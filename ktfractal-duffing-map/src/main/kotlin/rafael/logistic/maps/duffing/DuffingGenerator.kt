package rafael.logistic.maps.duffing

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.pow

data class DuffingParameter(val alpha: Double, val beta: Double) : IterationParameter

class DuffingGenerator : IterationGeneratorBi<DuffingParameter>() {

    override fun calculate(parameter: DuffingParameter, point: BiDouble): BiDouble =
            BiDouble(point.y, -parameter.beta * point.x + parameter.alpha * point.y - point.y.pow(3))

    fun generate(p0: BiDouble, alpha: Double, beta: Double, iterations: Int) =
            super.generate(p0, DuffingParameter(alpha, beta), iterations)

}
