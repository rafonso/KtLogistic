package rafael.logistic.maps.standard

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.IterationParameter
import kotlin.math.PI
import kotlin.math.sin

data class StandardParameter(val k: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Standard_map
// http://mathworld.wolfram.com/StandardMap.html
class StandardGenerator : IterationGeneratorBi<StandardParameter>() {

    override fun calculate(parameter: StandardParameter, point: BiPoint): BiPoint {
        val y = point.y + parameter.k * sin(point.x) / (2 * PI)
        return BiPoint((point.x + y), y)
    }

    fun generate(p0: BiPoint, k: Double, iterations: Int) =
            super.generate(p0, StandardParameter(k), iterations)

}
