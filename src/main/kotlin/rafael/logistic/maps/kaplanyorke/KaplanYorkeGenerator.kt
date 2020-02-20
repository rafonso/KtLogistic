package rafael.logistic.maps.kaplanyorke

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IteractionGeneratorBi
import rafael.logistic.generator.IteractionParameterBi
import kotlin.math.PI
import kotlin.math.cos

data class KaplanYorkeParameter(val alpha: Double) : IteractionParameterBi

// https://en.wikipedia.org/wiki/Kaplan%E2%80%93Yorke_map
class KaplanYorkeGenerator : IteractionGeneratorBi<KaplanYorkeParameter>() {

    companion object {
        fun calc(alpha: Double, p: BiPoint) =
                BiPoint(2 * p.x % 0.99995, alpha * p.y + cos(4 * PI * p.x))
    }

    override fun calculate(parameter: KaplanYorkeParameter, point: BiPoint): BiPoint =
            calc(parameter.alpha, point)

    fun generate(p0: BiPoint, alpha: Double, iteractions: Int) =
            super.generate(p0, KaplanYorkeParameter(alpha), iteractions)

}