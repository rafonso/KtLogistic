package rafael.logistic.maps.duffing

import javafx.geometry.Point2D
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.pow

data class DuffingParameter(val alpha: Double, val beta: Double) : IterationParameter

class DuffingGenerator : IterationGeneratorBi<DuffingParameter>() {

    override fun calculate(parameter: DuffingParameter, point: Point2D): Point2D =
            Point2D(point.y, -parameter.beta * point.x + parameter.alpha * point.y - point.y.pow(3))

    fun generate(p0: Point2D, alpha: Double, beta: Double, iterations: Int) =
            super.generate(p0, DuffingParameter(alpha, beta), iterations)

}
