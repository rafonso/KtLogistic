package rafael.logistic.maps.julia

import javafx.geometry.Point2D
import rafael.logistic.view.IterationGenerator
import rafael.logistic.view.IterationParameter
import java.util.stream.Collectors

data class JuliaParameter(val c: Point2D,
                          val xMin: Double, val xMax: Double, val width: Int,
                          val yMin: Double, val yMax: Double, val height: Int) : IterationParameter {

    private val deltaX = (xMax - xMin) / width

    private val deltaY = (yMax - yMin) / height

    val xValues = (0..width).map { it * deltaX + xMin }
    val cols = xValues.indices.toList()

    val yValues = (0..height).map { it * deltaY + yMin }
    val rows = yValues.indices.toList()

}

class JuliaGenerator : IterationGenerator<Point2D, JuliaInfo, JuliaParameter> {

    private tailrec fun checkConvergence(zx: Double, zy: Double, cx: Double, cy: Double, convergenceSteps: Int, iteration: Int = 1): Int? {
        if (iteration == convergenceSteps) {
            return null
        }

        val nextZX = zx * zx - zy * zy + cx
        val nextZY = 2 * zx * zy + cy

        return if ((nextZX * nextZX + nextZY * nextZY) > 4.0) iteration
        else checkConvergence(nextZX, nextZY, cx, cy, convergenceSteps, iteration + 1)
    }

    override fun generate(z0: Point2D, parameter: JuliaParameter, interactions: Int): List<JuliaInfo> {
        val isMandelbrot = (z0.x == 0.0) && (z0.y == 0.0)
        val verifier: (Double, Double) -> Int? =
                if (isMandelbrot) { x, y -> checkConvergence(0.0, 0.0, x, y, interactions) }
                else { x, y -> checkConvergence(x, y, parameter.c.x, parameter.c.y,interactions) }

        return parameter.cols.parallelStream()
                .flatMap { col ->
                    val x = parameter.xValues[col]
                    parameter.rows.parallelStream()
                            .map { row ->
                                val y = parameter.yValues[row]
                                JuliaInfo(col, row, x, y, verifier(x, y)) }
                            .filter { ji -> !ji.converges }
                }.collect(Collectors.toList())
    }

}