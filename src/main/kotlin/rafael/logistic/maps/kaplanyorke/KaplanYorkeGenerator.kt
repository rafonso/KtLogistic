package rafael.logistic.maps.kaplanyorke

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.IterationParameter
import kotlin.math.PI
import kotlin.math.cos

data class KaplanYorkeParameter(val alpha: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Kaplan%E2%80%93Yorke_map
class KaplanYorkeGenerator : IterationGeneratorBi<KaplanYorkeParameter>() {

    override fun calculate(parameter: KaplanYorkeParameter, point: BiPoint): BiPoint =
            BiPoint(2 * point.x % 0.99995, parameter.alpha * point.y + cos(4 * PI * point.x))

    fun generate(p0: BiPoint, alpha: Double, iterations: Int) =
            super.generate(p0, KaplanYorkeParameter(alpha), iterations)

}
