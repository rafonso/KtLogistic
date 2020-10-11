package rafael.logistic.maps.sets

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.generation.IterationParameter
import java.util.stream.Collectors

data class JuliaParameter(
    val cX: Double, val cY: Double,
    val xMin: Double, val xMax: Double, val width: Int,
    val yMin: Double, val yMax: Double, val height: Int
) : IterationParameter {

    private val deltaX = (xMax - xMin) / width

    private val deltaY = (yMax - yMin) / height

    val xValues = (0..width).map { it * deltaX + xMin }
    internal val xValuesArray by lazy { xValues.toDoubleArray() }
    val cols = xValues.indices.toList()

    val yValues = (0..height).map { it * deltaY + yMin }
    internal val yValuesArray  by lazy { yValues.toDoubleArray() }
    val rows = yValues.indices.toList()

    val valuesToBeProcessed = xValues.size * yValues.size
}

abstract class JuliaGenerator : IterationGenerator<BiDouble, JuliaInfo, JuliaParameter> {

    private fun generateParallel(parameter: JuliaParameter, interactions: Int): List<JuliaInfo> {
        return parameter.cols.parallelStream()
            .flatMap { col ->
                val x = parameter.xValues[col]
                parameter.rows.parallelStream()
                    .map { row ->
                        val y = parameter.yValues[row]
                        JuliaInfo(
                            col,
                            row,
                            x,
                            y,
                            verify(x, y, parameter, interactions)
                        )
                    }
                    .filter { ji -> !ji.converges }
            }.collect(Collectors.toList())
    }

    private fun generateFromArray(parameter: JuliaParameter, interactions: Int): List<JuliaInfo> {
        val result = Array(parameter.xValues.size * parameter.yValues.size) { emptyJuliaInfo }
        var i = 0

        parameter.xValuesArray.forEachIndexed { col, x ->
            parameter.yValuesArray.forEachIndexed { row, y ->
                result[i] =
                    JuliaInfo(
                        col,
                        row,
                        x,
                        y,
                        verify(x, y, parameter, interactions)
                    )
                i++
            }
        }

        return result.filter { ji -> !ji.converges }
    }

    protected tailrec fun checkConvergence(
        zx: Double,
        zy: Double,
        cx: Double,
        cy: Double,
        convergenceSteps: Int,
        iteration: Int = 1
    ): Int? {
        if (iteration == convergenceSteps) {
            return null
        }

        val nextZX = zx * zx - zy * zy + cx
        val nextZY = 2 * zx * zy + cy

        return if ((nextZX * nextZX + nextZY * nextZY) > 4.0) iteration
        else checkConvergence(nextZX, nextZY, cx, cy, convergenceSteps, iteration + 1)
    }

    protected abstract fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int?

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun generate(z0: BiDouble, parameter: JuliaParameter, interactions: Int): List<JuliaInfo> =
        if ((parameter.valuesToBeProcessed) <= 5000) generateFromArray(parameter, interactions)
        else generateParallel(parameter, interactions)

}
