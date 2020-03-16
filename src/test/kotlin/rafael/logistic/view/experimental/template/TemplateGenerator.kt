package rafael.logistic.view.experimental.template

import javafx.geometry.Point2D
import rafael.logistic.view.IterationGeneratorBi
import rafael.logistic.view.IterationParameter
import java.util.*

data class TemplateParameter(val min: Double, val max: Double) : IterationParameter

class TemplateGenerator : IterationGeneratorBi<TemplateParameter>() {

    private val random = Random()

    override fun calculate(parameter: TemplateParameter, point: Point2D): Point2D {
        val value = (parameter.max - parameter.min) * random.nextDouble() + parameter.min
        val signalX = if (random.nextBoolean()) +1 else -1
        val signalY = if (random.nextBoolean()) +1 else -1

        return Point2D(signalX * value, signalY * value)
    }

    fun generate(p0: Point2D, min: Double, max: Double, iterations: Int) =
            super.generate(p0, TemplateParameter(min, max), iterations)

}
