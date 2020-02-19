package rafael.logistic.maps.henon

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.IteractionParameterBi

data class HenonParameter(val alpha: Double, val beta: Double) : IteractionParameterBi

class HenonGenerator : IteractionGeneratorBi<HenonParameter>() {

    companion object {
        fun calc(alpha: Double, beta: Double, p: BiPoint) =
                BiPoint(1.0 - alpha * p.x * p.x + p.y, beta * p.x)
    }

    override fun calculate(parameter: HenonParameter, point: BiPoint): BiPoint =
            calc(parameter.alpha, parameter.beta, point)

    fun generate(p0: BiPoint, alpha: Double, beta: Double, iteractions: Int) =
            super.generate(p0, HenonParameter(alpha, beta), iteractions)

}