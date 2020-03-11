package rafael.logistic.maps.tinkerbell

import javafx.geometry.Point2D
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.IterationParameter
import kotlin.math.pow

data class TinkerbellParameter(val a: Double, val b: Double, val c: Double, val d: Double) : IterationParameter
/**
 * https://en.wikipedia.org/wiki/Tinkerbell_map
 * - Valores Interessantes
 *      a = 0.9, b = - 0.6013, c = 2.0, d = 0.50, x0 = 0.72, y0 = - 0.64
 *      a = 0.5, b = + 0.5891, c = 2.3, d = 0.10, x0 = 0.11, y0 = + 0.50
 */
class TinkerbellGenerator : IterationGeneratorBi<TinkerbellParameter>() {

    override fun calculate(parameter: TinkerbellParameter, value: Point2D): Point2D =
            Point2D(
                    value.x.pow(2) - value.y.pow(2) + parameter.a * value.x + parameter.b * value.y,
                    2 * value.x * value.y + parameter.c * value.x + parameter.d * value.y
            )

    fun generate(p0: Point2D, a: Double, b: Double, c: Double, d: Double, iterations: Int) =
            super.generate(p0, TinkerbellParameter(a, b, c, d), iterations)

}
