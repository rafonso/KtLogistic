package rafael.logistic.generator

data class LogisticParameter(val r: Double) : IteractionParameter

class LogisticGenerator : IteractionGenerator<LogisticParameter>() {

    override fun calculate(parameter: LogisticParameter, xPrior: Double): Double = xPrior * parameter.r * (1.0 - xPrior)

    fun generate(x0: Double, r: Double, iteractions: Int): List<Double> = super.generate(x0, LogisticParameter(r), iteractions)

}