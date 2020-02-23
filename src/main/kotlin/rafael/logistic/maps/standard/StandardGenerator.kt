package rafael.logistic.maps.standard

import rafael.logistic.generator.BiPoint
import rafael.logistic.generator.IterationGeneratorBi
import rafael.logistic.generator.IterationParameter
import kotlin.math.PI
import kotlin.math.sin

data class StandardParameter(val k: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Standard_map
// http://mathworld.wolfram.com/StandardMap.html
class StandardGenerator : IterationGeneratorBi<StandardParameter>() {

    companion object {

        const val CICLE = 2 * PI

        fun calc(k: Double, p: BiPoint) =
                BiPoint((p.x + p.y) % CICLE, (p.y + k * sin(p.x)) % CICLE)
    }

    override fun calculate(parameter: StandardParameter, point: BiPoint): BiPoint =
            calc(parameter.k, point)

    fun generate(p0: BiPoint, k: Double, iterations: Int) =
            super.generate(p0, StandardParameter(k), iterations)

}
