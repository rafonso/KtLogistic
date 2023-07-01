package rafael.logistic.map.standard

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.PI
import kotlin.math.sin

const val MIN_K = 0.0
const val MAX_K = 10.0

data class StandardMapParameter(val k: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Standard_map
// http://mathworld.wolfram.com/StandardMap.html
class StandardMapGenerator : IterationGeneratorBi<StandardMapParameter>() {

    override fun calculate(
        parameter: StandardMapParameter,
        point: BiDouble
    ): BiDouble {
        val y = point.y + parameter.k * sin(point.x) / (2 * PI)
        return BiDouble((point.x + y), y)
    }

    fun generate(p0: BiDouble, k: Double, iterations: Int) =
        super.generate(p0, StandardMapParameter(k), iterations)

}
