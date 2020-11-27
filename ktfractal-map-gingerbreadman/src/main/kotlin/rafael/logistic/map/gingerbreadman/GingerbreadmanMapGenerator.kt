package rafael.logistic.map.gingerbreadman

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.generation.IterationGeneratorBi
import rafael.logistic.core.generation.NoParameter
import kotlin.math.absoluteValue

class GingerbreadmanMapGenerator : IterationGeneratorBi<NoParameter>() {

    override fun calculate(
        parameter: NoParameter,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") point: BiDouble
    ): BiDouble =
        BiDouble(1.0 - point.y + point.x.absoluteValue, point.x)

    fun generate(p0: BiDouble, iterations: Int) =
        super.generate(p0, NoParameter, iterations)

}
