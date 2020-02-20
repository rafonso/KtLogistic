package rafael.logistic.maps.tinkerbell

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.IteractionParameterBi
import kotlin.math.pow

data class TinkerbellParameter(val a: Double, val b: Double, val c: Double, val d: Double) : IteractionParameterBi

class TinkerbellGenerator : IteractionGeneratorBi<TinkerbellParameter>() {

    companion object {
        fun calc(a: Double, b: Double, c: Double, d: Double, p: BiPoint) =
                BiPoint(
                        p.x.pow(2) - p.y.pow(2) + a * p.x + b * p.y,
                        2 * p.x * p.y + c * p.x + d * p.y
                )
    }

    override fun calculate(parameter: TinkerbellParameter, point: BiPoint): BiPoint =
            calc(parameter.a, parameter.b, parameter.c, parameter.c, point)

    fun generate(p0: BiPoint, a: Double, b: Double, c: Double, d: Double, iteractions: Int) =
            super.generate(p0, TinkerbellParameter(a, b, c, d), iteractions)

}