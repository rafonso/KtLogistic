package rafael.logistic.maps.gingerbreadman

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.NoParameterBi
import kotlin.math.absoluteValue

class GingerbreadmanGenerator : IteractionGeneratorBi<NoParameterBi>() {

    companion object {
        fun calc(p: BiPoint) = BiPoint(1.0 - p.y + p.x.absoluteValue, p.x)
    }

    override fun calculate(parameter: NoParameterBi, point: BiPoint): BiPoint = calc(point)

    fun generate(p0: BiPoint, iteractions: Int) = super.generate(p0, NoParameterBi, iteractions)

}