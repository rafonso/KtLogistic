package rafael.logistic.maps.gingerbreadman

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.NoParameter
import kotlin.math.absoluteValue

class GingerbreadmanGenerator : IterationGeneratorBi<NoParameter>() {

    override fun calculate(parameter: NoParameter, point: BiPoint): BiPoint =
            BiPoint(1.0 - point.y + point.x.absoluteValue, point.x)

    fun generate(p0: BiPoint, iterations: Int) = super.generate(p0, NoParameter, iterations)

}
