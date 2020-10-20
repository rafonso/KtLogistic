package rafael.logistic.map.mandelbrot

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.pow

data class MandelbrotParameter(val real: Double, val im: Double) : IterationParameter

class MandelbrotGenerator : IterationGeneratorBi<MandelbrotParameter>() {

    override fun calculate(parameter: MandelbrotParameter, value: BiDouble): BiDouble =
            BiDouble(
                    value.x.pow(2) - value.y.pow(2) + parameter.real,
                    2 * value.x * value.y + parameter.im
            )

    fun generate(p0: BiDouble, re: Double, im: Double, iterations: Int) =
            super.generate(p0, MandelbrotParameter(re, im), iterations)

}
