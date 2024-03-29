package rafael.logistic.map.henon

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter

data class HenonParameter(val alpha: Double, val beta: Double) : IterationParameter

class HenonMapGenerator : IterationGeneratorBi<HenonParameter>() {

    override fun calculate(
        parameter: HenonParameter,
        point: BiDouble
    ): BiDouble =
        BiDouble(1.0 - parameter.alpha * point.x * point.x + point.y, parameter.beta * point.x)

    fun generate(p0: BiDouble, alpha: Double, beta: Double, iterations: Int) =
        super.generate(p0, HenonParameter(alpha, beta), iterations)

}
