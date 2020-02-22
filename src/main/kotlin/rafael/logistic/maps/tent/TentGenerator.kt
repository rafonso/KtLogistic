package rafael.logistic.maps.tent

import rafael.logistic.generator.IteractionGeneratorDouble
import rafael.logistic.generator.IterationParameter

data class TentParameter(val mi: Double) : IterationParameter

class TentGenerator : IteractionGeneratorDouble<TentParameter>() {

    companion object {
        fun calc(mi: Double, x: Double): Double = if (x <= 0.5) mi * x else mi * (1 - x)
    }

    override fun calculate(parameter: TentParameter, x: Double): Double = calc(parameter.mi, x)

    fun generate(x0: Double, mi: Double, iteractions: Int): List<Double> =
            super.generate(x0, TentParameter(mi), iteractions)

}
