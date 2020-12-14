package rafael.logistic.set

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.generation.IterationParameter
import java.util.stream.Collectors

// private const val MAX_VALUE_FOR_ARRAY = 5_000

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
data class SetParameter(
    val cX: Double, val cY: Double,
    val xMin: Double, val xMax: Double, val width: Int,
    val yMin: Double, val yMax: Double, val height: Int
) : IterationParameter {

    private val deltaX = (xMax - xMin) / width

    private val deltaY = (yMax - yMin) / height

    val xValues = (0 until width).map { it * deltaX + xMin }.toDoubleArray()

    //    internal val xValuesArray by lazy { xValues.toDoubleArray() }
    val cols = xValues.indices.toList()

    val yValues = (0 until height).map { it * deltaY + yMin }.toDoubleArray()

    //    internal val yValuesArray by lazy { yValues.toDoubleArray() }
    val rows = yValues.indices.toList()

//    val valuesToBeProcessed = xValues.size * yValues.size
}

/**
 * Julia generator
 *
 * @constructor Create empty Julia generator
 */
abstract class SetGenerator : IterationGenerator<BiDouble, SetInfo, SetParameter> {

    private fun generateParallel(parameter: SetParameter, interactions: Int): List<SetInfo> =
        parameter.rows.parallelStream()
            .flatMap { row ->
                val y = parameter.yValues[parameter.rows.size - row - 1]
                parameter.cols.parallelStream()
                    .map { col ->
                        val x = parameter.xValues[col]
                        val iterationsToDiverge = verify(x, y, parameter, interactions)

                        SetInfo(col, row, x, y, iterationsToDiverge)
                    }
            }
            .collect(Collectors.toList())

    /**
     * Check convergence
     *
     * @param zx valor da coordenada `x` do ponto corrente.
     * @param zy valor da coordenada `y` do ponto corrente.
     * @param cx valor da coordenada `x` da constante.
     * @param cy valor da coordenada `y` da constante.
     * @param convergenceSteps Número máximo de iterações
     * @param iteration Iteração corrente (será incrementado a cada passo)
     * @return Número de Iterações para a série divergir ou `0` se ela não o fez até atingir [convergenceSteps].
     */
    protected tailrec fun checkConvergence(
        zx: Double,
        zy: Double,
        cx: Double,
        cy: Double,
        convergenceSteps: Int,
        iteration: Int = 1
    ): Int =
        if (iteration == convergenceSteps) 0
        else {
            val nextZX = nextX(zx, zy, cx, cy)
            val nextZY = nextY(zx, zy, cx, cy)

            if ((nextZX * nextZX + nextZY * nextZY) > 4.0) iteration
            else checkConvergence(nextZX, nextZY, cx, cy, convergenceSteps, iteration + 1)
        }

    protected abstract fun nextX(zx: Double, zy: Double, cx: Double, cy: Double): Double

    protected abstract fun nextY(zx: Double, zy: Double, cx: Double, cy: Double): Double

    /**
     * Verifica quanta iterações são necessárias para a série divirga em um determinado ponto.
     *
     * @param x coordenada x do ponto
     * @param y coordenada y do ponto
     * @param parameter Parâmetros da série
     * @param interactions Quantidade máxima de Iterações.
     * @return o número de iterações necessárias para que a série divirga ou `null` se ela não divergir ao se atingir [interactions]
     */
    protected abstract fun verify(x: Double, y: Double, parameter: SetParameter, interactions: Int): Int

    override fun generate(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") z0: BiDouble,
        parameter: SetParameter,
        interactions: Int
    ): List<SetInfo> = generateParallel(parameter, interactions)

}

/*
    private fun generateFromArray(parameter: SetParameter, interactions: Int): List<SetInfo> {
        val result = Array(parameter.xValues.size * parameter.yValues.size) { emptySetInfo }
        var i = 0

        parameter.xValues.forEachIndexed { col, x ->
            parameter.yValues.forEachIndexed { row, y ->
                val iterationsToDiverge = verify(x, y, parameter, interactions)
                result[i] = SetInfo(col, row, x, y, iterationsToDiverge)
                i++
            }
        }

        return result.filter { ji -> !ji.converges }
    }


 */