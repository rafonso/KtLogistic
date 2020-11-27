package rafael.experimental.template

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.random.Random

data class TemplateParameter(val min: Double, val max: Double) : IterationParameter

class TemplateGenerator : IterationGeneratorBi<TemplateParameter>() {

    override fun calculate(parameter: TemplateParameter, @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") point: BiDouble): BiDouble {
        val value = (parameter.max - parameter.min) * Random.nextDouble() + parameter.min
        val signalX = if (Random.nextBoolean()) +1 else -1
        val signalY = if (Random.nextBoolean()) +1 else -1

        return BiDouble(signalX * value, signalY * value)
    }

    fun generate(p0: BiDouble, min: Double, max: Double, iterations: Int) =
            super.generate(p0, TemplateParameter(min, max), iterations)

}
