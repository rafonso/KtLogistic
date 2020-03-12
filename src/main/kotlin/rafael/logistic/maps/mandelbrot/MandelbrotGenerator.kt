package rafael.logistic.maps.mandelbrot

import javafx.geometry.Point2D
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.IterationParameter
import kotlin.math.pow

data class MandelbrotParameter(val real: Double, val im: Double) : IterationParameter

class MandelbrotGenerator : IterationGeneratorBi<MandelbrotParameter>() {

    override fun calculate(parameter: MandelbrotParameter, value: Point2D): Point2D =
            Point2D(
                    value.x.pow(2) - value.y.pow(2) + parameter.real,
                    2 * value.x * value.y + parameter.im
            )

    fun generate(p0: Point2D, re: Double, im: Double, iterations: Int) =
            super.generate(p0, MandelbrotParameter(re, im), iterations)

}
