package rafael.logistic.maps.lozi

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.IteractionParameterBi
import kotlin.math.absoluteValue

data class LoziParameter(val alpha: Double, val beta: Double) : IteractionParameterBi

class LoziGenerator : IteractionGeneratorBi<LoziParameter>() {

    companion object {
        fun calc(alpha: Double, beta: Double, p: BiPoint) =
                BiPoint(1.0 - alpha * p.x.absoluteValue + p.y, beta * p.x)
    }

    override fun calculate(parameter: LoziParameter, point: BiPoint): BiPoint =
            calc(parameter.alpha, parameter.beta, point)

    fun generate(p0: BiPoint, alpha: Double, beta: Double, iteractions: Int) =
            super.generate(p0, LoziParameter(alpha, beta), iteractions)

}