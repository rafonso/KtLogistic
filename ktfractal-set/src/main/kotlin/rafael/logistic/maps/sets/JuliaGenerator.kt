package rafael.logistic.maps.sets

import javafx.geometry.Point2D
import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.generation.IterationParameter
import java.util.stream.Collectors

data class JuliaParameter(val cX: Double, val cY: Double,
                          val xMin: Double, val xMax: Double, val width: Int,
                          val yMin: Double, val yMax: Double, val height: Int) : IterationParameter {

    private val deltaX = (xMax - xMin) / width

    private val deltaY = (yMax - yMin) / height

    val xValues = (0..width).map { it * deltaX + xMin }
    val cols = xValues.indices.toList()

    val yValues = (0..height).map { it * deltaY + yMin }
    val rows = yValues.indices.toList()

}

abstract class JuliaGenerator : IterationGenerator<Point2D, JuliaInfo, JuliaParameter> {

    protected tailrec fun checkConvergence(zx: Double, zy: Double, cx: Double, cy: Double, convergenceSteps: Int, iteration: Int = 1): Int? {
        if (iteration == convergenceSteps) {
            return null
        }

        val nextZX = zx * zx - zy * zy + cx
        val nextZY = 2 * zx * zy + cy

        return if ((nextZX * nextZX + nextZY * nextZY) > 4.0) iteration
        else checkConvergence(nextZX, nextZY, cx, cy, convergenceSteps, iteration + 1)
    }

    protected abstract fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int?

    override fun generate(z0: Point2D, parameter: JuliaParameter, interactions: Int): List<JuliaInfo> {
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

}
