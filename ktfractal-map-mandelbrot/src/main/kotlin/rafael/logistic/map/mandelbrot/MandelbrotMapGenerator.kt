package rafael.logistic.map.mandelbrot

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.pow

data class MandelbrotMapParameter(val real: Double, val im: Double) : IterationParameter

class MandelbrotMapGenerator : IterationGeneratorBi<MandelbrotMapParameter>() {

    override fun calculate(parameter: MandelbrotMapParameter, value: BiDouble): BiDouble =
            BiDouble(
                    value.x.pow(2) - value.y.pow(2) + parameter.real,
                    2 * value.x * value.y + parameter.im
            )

    fun generate(p0: BiDouble, re: Double, im: Double, iterations: Int) =
            super.generate(p0, MandelbrotMapParameter(re, im), iterations)

}
