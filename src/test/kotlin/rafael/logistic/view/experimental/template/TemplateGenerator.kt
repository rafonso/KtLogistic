package rafael.logistic.view.experimental.template

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.IterationParameter
import java.util.*

data class TemplateParameter(val min: Double, val max: Double) : IterationParameter

class TemplateGenerator : IterationGeneratorBi<TemplateParameter>() {

    private val random = Random()

    override fun calculate(parameter: TemplateParameter, point: BiPoint): BiPoint {
        val value = (parameter.max - parameter.min) * random.nextDouble() + parameter.min
        val signalX = if (random.nextBoolean()) +1 else -1
        val signalY = if (random.nextBoolean()) +1 else -1

        return BiPoint(signalX * value, signalY * value)
    }

    fun generate(p0: BiPoint, min: Double, max: Double, iterations: Int) =
            super.generate(p0, TemplateParameter(min, max), iterations)

}
