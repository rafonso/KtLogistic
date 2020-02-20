package rafael.logistic.maps.duffing

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.IteractionParameterBi
import kotlin.math.pow

data class DuffingParameter(val alpha: Double, val beta: Double) : IteractionParameterBi

class DuffingGenerator : IteractionGeneratorBi<DuffingParameter>() {

    companion object {
        fun calc(alpha: Double, beta: Double, p: BiPoint) =
                BiPoint(p.y, - beta * p.x + alpha * p.y - p.y.pow(3))
    }

    override fun calculate(parameter: DuffingParameter, point: BiPoint): BiPoint =
            calc(parameter.alpha, parameter.beta, point)

    fun generate(p0: BiPoint, alpha: Double, beta: Double, iteractions: Int) =
            super.generate(p0, DuffingParameter(alpha, beta), iteractions)

}