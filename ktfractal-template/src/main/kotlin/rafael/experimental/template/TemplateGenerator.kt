package rafael.experimental.template

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import java.util.*

data class TemplateParameter(val min: Double, val max: Double) : IterationParameter

class TemplateGenerator : IterationGeneratorBi<TemplateParameter>() {

    private val random = Random()

    override fun calculate(parameter: TemplateParameter, point: BiDouble): BiDouble {
        val value = (parameter.max - parameter.min) * random.nextDouble() + parameter.min
        val signalX = if (random.nextBoolean()) +1 else -1
        val signalY = if (random.nextBoolean()) +1 else -1

        return BiDouble(signalX * value, signalY * value)
    }

    fun generate(p0: BiDouble, min: Double, max: Double, iterations: Int) =
            super.generate(p0, TemplateParameter(min, max), iterations)

}
