package rafael.logistic.maps.gingerbreadman

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.NoParameter
import kotlin.math.absoluteValue

class GingerbreadmanGenerator : IterationGeneratorBi<NoParameter>() {

    override fun calculate(parameter: NoParameter, point: BiDouble): BiDouble =
        BiDouble(1.0 - point.y + point.x.absoluteValue, point.x)

    fun generate(p0: BiDouble, iterations: Int) =
        super.generate(p0, NoParameter, iterations)

}
