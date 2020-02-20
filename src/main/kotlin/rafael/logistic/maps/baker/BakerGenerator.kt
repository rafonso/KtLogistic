package rafael.logistic.maps.baker

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.IteractionParameterBi
import kotlin.math.pow

object BakerParameter : IteractionParameterBi

class BakerGenerator : IteractionGeneratorBi<BakerParameter>() {

    companion object {
        fun calc(p: BiPoint) =
                if (p.x < 0.5) BiPoint(2 * p.x, p.y / 2)
                else BiPoint(2 * (1.0 - p.x), 1.0 - p.y / 2)
    }

    override fun calculate(parameter: BakerParameter, point: BiPoint): BiPoint =
            calc(point)

    fun generate(p0: BiPoint, iteractions: Int) =
            super.generate(p0, BakerParameter, iteractions)

}