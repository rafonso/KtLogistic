package rafael.logistic.map.set

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.generation.IterationParameter
import java.util.stream.Collectors

private const val MAX_VALUE_FOR_ARRAY = 5_000

/**
 * Julia parameter
 *
 * @property cX
 * @property cY
 * @property xMin
 * @property xMax
 * @property width
 * @property yMin
 * @property yMax
 * @property height
 * @constructor Create empty Julia parameter
 */
data class JuliaParameter(
    val cX: Double, val cY: Double,
    val xMin: Double, val xMax: Double, val width: Int,
    val yMin: Double, val yMax: Double, val height: Int
) : IterationParameter {

    private val deltaX = (xMax - xMin) / width

    private val deltaY = (yMax - yMin) / height

    val xValues = (0..width).map { it * deltaX + xMin }.toDoubleArray()

    //    internal val xValuesArray by lazy { xValues.toDoubleArray() }
    val cols = xValues.indices.toList()

    val yValues = (0..height).map { it * deltaY + yMin }.toDoubleArray()

    //    internal val yValuesArray by lazy { yValues.toDoubleArray() }
    val rows = yValues.indices.toList()

    val valuesToBeProcessed = xValues.size * yValues.size
}

/**
 * Julia generator
 *
 * @constructor Create empty Julia generator
 */
abstract class JuliaGenerator : IterationGenerator<BiDouble, JuliaInfo, JuliaParameter> {

    private fun generateParallel(parameter: JuliaParameter, interactions: Int): List<JuliaInfo> {
        return parameter.cols.parallelStream()
            .flatMap { col ->
                val x = parameter.xValues[col]
                parameter.rows.parallelStream()
                    .map { row ->
                        val y = parameter.yValues[row]
                        val iterationsToDiverge = verify(x, y, parameter, interactions)

                        JuliaInfo(col, row, x, y, iterationsToDiverge)
                    }
            }
            .filter { ji -> !ji.converges }
            .collect(Collectors.toList())
    }

    private fun generateFromArray(parameter: JuliaParameter, interactions: Int): List<JuliaInfo> {
        val result = Array(parameter.xValues.size * parameter.yValues.size) { emptyJuliaInfo }
        var i = 0

        parameter.xValues.forEachIndexed { col, x ->
            parameter.yValues.forEachIndexed { row, y ->
                val iterationsToDiverge = verify(x, y, parameter, interactions)
                result[i] = JuliaInfo(col, row, x, y, iterationsToDiverge)
                i++
            }
        }

        return result.filter { ji -> !ji.converges }
    }

    /**
     * Check convergence
     *
     * @param zx valor da coordenada `x` do ponto corrente.
     * @param zy valor da coordenada `y` do ponto corrente.
     * @param cx valor da coordenada `x` da constante.
     * @param cy valor da coordenada `y` da constante.
     * @param convergenceSteps Número máximo de iterações
     * @param iteration Iteração corrente (será incrementado a cada passo)
     * @return Número de Iterações para a série divergir ou `null` se ela não o fez até atingir [convergenceSteps].
     */
    protected tailrec fun checkConvergence(
        zx: Double,
        zy: Double,
        cx: Double,
        cy: Double,
        convergenceSteps: Int,
        iteration: Int = 1
    ): Int? =
        if (iteration == convergenceSteps) null
        else {
            val nextZX = zx * zx - zy * zy + cx
            val nextZY = 2 * zx * zy + cy

            if ((nextZX * nextZX + nextZY * nextZY) > 4.0) iteration
            else checkConvergence(nextZX, nextZY, cx, cy, convergenceSteps, iteration + 1)
        }

    /**
     * Verifica quanta iterações são necessárias para a série divirga em um determinado ponto.
     *
     * @param x coordenada x do ponto
     * @param y coordenada y do ponto
     * @param parameter Parâmetros da série
     * @param interactions Quantidade máxima de Iterações.
     * @return o número de iterações necessárias para que a série divirga ou `null` se ela não divergir ao se atingir [interactions]
     */
    protected abstract fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int?

    override fun generate(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") z0: BiDouble,
        parameter: JuliaParameter,
        interactions: Int
    ): List<JuliaInfo> =
        if (parameter.valuesToBeProcessed <= MAX_VALUE_FOR_ARRAY) generateFromArray(parameter, interactions)
        else generateParallel(parameter, interactions)

}
