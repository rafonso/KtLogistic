package rafael.logistic.map.baker

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.NoParameter

class BakerMapGenerator : IterationGeneratorBi<NoParameter>() {

    companion object {
        fun calc(p: BiDouble) =
            when (p.x) {
                in 0.0..0.5 -> BiDouble(+2 * p.x, +p.y / 2)
                in 0.5..1.0 -> BiDouble(-2 * p.x + 2, -p.y / 2 + 1.0)
                else        -> error("$p")
            }
    }

    override fun calculate(
        parameter: NoParameter,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") point: BiDouble
    ): BiDouble =
        calc(point)

    fun generate(p0: BiDouble, iterations: Int) =
        super.generate(p0, NoParameter, iterations)

}
