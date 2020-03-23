package rafael.logistic.maps.gingerbreadman

import javafx.geometry.Point2D
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.NoParameter
import kotlin.math.absoluteValue

class GingerbreadmanGenerator : IterationGeneratorBi<NoParameter>() {

    override fun calculate(parameter: NoParameter, point: Point2D): Point2D =
            Point2D(1.0 - point.y + point.x.absoluteValue, point.x)

    fun generate(p0: Point2D, iterations: Int) = super.generate(p0, NoParameter, iterations)

}
