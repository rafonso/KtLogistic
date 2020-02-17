package rafael.logistic.generator

data class LogisticParameter(val r: Double) : IteractionParameter

class LogisticGenerator : IteractionGenerator<LogisticParameter>() {

    override fun calculate(parameter: LogisticParameter, x: Double): Double = x * parameter.r * (1.0 - x)

    fun generate(x0: Double, r: Double, iteractions: Int): List<Double> = super.generate(x0, LogisticParameter(r), iteractions)

}