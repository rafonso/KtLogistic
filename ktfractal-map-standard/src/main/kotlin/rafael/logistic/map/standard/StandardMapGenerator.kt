package rafael.logistic.map.standard

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.PI
import kotlin.math.sin

data class StandardMapParameter(val k: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Standard_map
// http://mathworld.wolfram.com/StandardMap.html
class StandardMapGenerator : IterationGeneratorBi<StandardMapParameter>() {

    override fun calculate(
        parameter: StandardMapParameter,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") point: BiDouble
    ): BiDouble {
        val y = point.y + parameter.k * sin(point.x) / (2 * PI)
        return BiDouble((point.x + y), y)
    }

    fun generate(p0: BiDouble, k: Double, iterations: Int) =
        super.generate(p0, StandardMapParameter(k), iterations)

}
