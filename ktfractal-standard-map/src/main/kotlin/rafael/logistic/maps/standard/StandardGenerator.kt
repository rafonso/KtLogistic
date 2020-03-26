package rafael.logistic.maps.standard

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.PI
import kotlin.math.sin

data class StandardParameter(val k: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Standard_map
// http://mathworld.wolfram.com/StandardMap.html
class StandardGenerator : IterationGeneratorBi<StandardParameter>() {

    override fun calculate(parameter: StandardParameter, point: BiDouble): BiDouble {
        val y = point.y + parameter.k * sin(point.x) / (2 * PI)
        return BiDouble((point.x + y), y)
    }

    fun generate(p0: BiDouble, k: Double, iterations: Int) =
            super.generate(p0, StandardParameter(k), iterations)

}
