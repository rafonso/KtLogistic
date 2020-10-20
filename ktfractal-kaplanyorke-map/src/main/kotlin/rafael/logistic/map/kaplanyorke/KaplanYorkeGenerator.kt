package rafael.logistic.map.kaplanyorke

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.PI
import kotlin.math.cos

data class KaplanYorkeParameter(val alpha: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Kaplan%E2%80%93Yorke_map
class KaplanYorkeGenerator : IterationGeneratorBi<KaplanYorkeParameter>() {

    override fun calculate(parameter: KaplanYorkeParameter, point: BiDouble): BiDouble =
            BiDouble(2 * point.x % 0.99995, parameter.alpha * point.y + cos(4 * PI * point.x))

    fun generate(p0: BiDouble, alpha: Double, iterations: Int) =
            super.generate(p0, KaplanYorkeParameter(alpha), iterations)

}
