package rafael.logistic.maps.tinkerbell

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.IterationParameter
import kotlin.math.pow

data class TinkerbellParameter(val a: Double, val b: Double, val c: Double, val d: Double) : IterationParameter

class TinkerbellGenerator : IterationGeneratorBi<TinkerbellParameter>() {

    override fun calculate(parameter: TinkerbellParameter, point: BiPoint): BiPoint =
            BiPoint(
                    point.x.pow(2) - point.y.pow(2) + parameter.a * point.x + parameter.b * point.y,
                    2 * point.x * point.y + parameter.c * point.x + parameter.c * point.y
            )

    fun generate(p0: BiPoint, a: Double, b: Double, c: Double, d: Double, iterations: Int) =
            super.generate(p0, TinkerbellParameter(a, b, c, d), iterations)

}