package rafael.logistic.generator

data class TentParameter(val mi: Double) : IteractionParameter

class TentGenerator : IteractionGenerator<TentParameter>() {

    companion object {
        fun calc(mi: Double, x: Double): Double = if (x <= 0.5) mi * x else mi * (1 - x)
    }

    override fun calculate(parameter: TentParameter, x: Double): Double = calc(parameter.mi, x)

    fun generate(x0: Double, mi: Double, iteractions: Int): List<Double> =
            super.generate(x0, TentParameter(mi), iteractions)

}