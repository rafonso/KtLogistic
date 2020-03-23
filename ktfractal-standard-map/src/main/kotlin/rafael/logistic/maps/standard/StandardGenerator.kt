package rafael.logistic.maps.standard

import javafx.geometry.Point2D
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.PI
import kotlin.math.sin

data class StandardParameter(val k: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Standard_map
// http://mathworld.wolfram.com/StandardMap.html
class StandardGenerator : IterationGeneratorBi<StandardParameter>() {

    override fun calculate(parameter: StandardParameter, point: Point2D): Point2D {
        val y = point.y + parameter.k * sin(point.x) / (2 * PI)
        return Point2D((point.x + y), y)
    }

    fun generate(p0: Point2D, k: Double, iterations: Int) =
            super.generate(p0, StandardParameter(k), iterations)

}
