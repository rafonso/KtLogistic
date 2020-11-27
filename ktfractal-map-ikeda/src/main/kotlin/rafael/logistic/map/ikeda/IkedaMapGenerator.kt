package rafael.logistic.map.ikeda

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.generation.IterationGeneratorBi
import rafael.logistic.core.generation.IterationParameter
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

const val U_MAX = +1.0
const val U_MIN = -1.0

data class IkedaParameter(val u: Double) : IterationParameter

// https://en.wikipedia.org/wiki/Ikeda_map
class IkedaMapGenerator : IterationGeneratorBi<IkedaParameter>() {

    override fun calculate(
        parameter: IkedaParameter,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") point: BiDouble
    ): BiDouble {
        val t = 0.4 - 6.0 / (1 + point.x.pow(2) +  point.y.pow(2))

        return BiDouble(
            1.0 + parameter.u * (point.x * cos(t) - point.y * sin(t)),
            parameter.u * (point.x * sin(t) + point.y * cos(t))
        )
    }

    fun generate(p0: BiDouble, u: Double, iterations: Int) =
        super.generate(p0, IkedaParameter(u), iterations)

}
