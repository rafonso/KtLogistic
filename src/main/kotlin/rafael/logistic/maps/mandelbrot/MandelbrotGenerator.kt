package rafael.logistic.maps.mandelbrot

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGenerator
import rafael.logistic.generator.IterationParameter
import kotlin.math.pow

data class MandelbrotParameter(val real: Double, val im: Double) : IterationParameter

class MandelbrotGenerator : IterationGenerator<BiPoint, MandelbrotParameter>() {

    override fun calculate(parameter: MandelbrotParameter, value: BiPoint): BiPoint =
            BiPoint(
                    value.x.pow(2) - value.y.pow(2) + parameter.real,
                    2 * value.x * value.y + parameter.im
            )

    fun generate(p0: BiPoint, re: Double, im: Double, iterations: Int) =
            super.generate(p0, MandelbrotParameter(re, im), iterations)

}
