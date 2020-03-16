package rafael.logistic.maps.kaplanyorke

import javafx.geometry.Point2D
import rafael.logistic.view.IterationGeneratorBi
import rafael.logistic.view.IterationParameter
import kotlin.math.PI
import kotlin.math.cos

data class KaplanYorkeParameter(val alpha: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Kaplan%E2%80%93Yorke_map
class KaplanYorkeGenerator : IterationGeneratorBi<KaplanYorkeParameter>() {

    override fun calculate(parameter: KaplanYorkeParameter, point: Point2D): Point2D =
            Point2D(2 * point.x % 0.99995, parameter.alpha * point.y + cos(4 * PI * point.x))

    fun generate(p0: Point2D, alpha: Double, iterations: Int) =
            super.generate(p0, KaplanYorkeParameter(alpha), iterations)

}
