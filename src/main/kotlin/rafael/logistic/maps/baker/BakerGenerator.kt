package rafael.logistic.maps.baker

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.NoParameter

class BakerGenerator : IterationGeneratorBi<NoParameter>() {

    companion object {
        fun calc(p: BiPoint) =
                when (p.x) {
                    in 0.0..0.5 -> BiPoint(+ 2 * p.x    , + p.y / 2)
                    in 0.5..1.0 -> BiPoint(- 2 * p.x + 2, - p.y / 2 + 1.0)
                    else        -> error("$p")
                }
    }

    override fun calculate(parameter: NoParameter, point: BiPoint): BiPoint =
            calc(point)

    fun generate(p0: BiPoint, iterations: Int) =
            super.generate(p0, NoParameter, iterations)

}
