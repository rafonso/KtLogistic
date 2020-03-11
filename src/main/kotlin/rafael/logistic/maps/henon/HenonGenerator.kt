package rafael.logistic.maps.henon

import javafx.geometry.Point2D
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.IterationParameter

data class HenonParameter(val alpha: Double, val beta: Double) : IterationParameter

class HenonGenerator : IterationGeneratorBi<HenonParameter>() {

    override fun calculate(parameter: HenonParameter, point: Point2D): Point2D =
            Point2D(1.0 - parameter.alpha * point.x * point.x + point.y, parameter.beta * point.x)

    fun generate(p0: Point2D, alpha: Double, beta: Double, iterations: Int) =
            super.generate(p0, HenonParameter(alpha, beta), iterations)

}
