package rafael.logistic.maps.julia

import javafx.geometry.Point2D
import rafael.logistic.view.IterationGenerator
import rafael.logistic.view.IterationParameter
import java.util.stream.Collectors

data class JuliaParameter(val c: Point2D,
                          val xMin: Double, val xMax: Double, val xScale: Int,
                          val yMin: Double, val yMax: Double, val yScale: Int) : IterationParameter {

    private val deltaX = (xMax - xMin) / xScale

    private val deltaY = (yMax - yMin) / yScale

    val xValues = (0..xScale).map { it * deltaX + xMin }

    val yValues = (0..yScale).map { it * deltaY + yMin }

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

        return parameter.xValues.parallelStream()
                .flatMap { x ->
                    parameter.yValues.parallelStream()
                            .map { y -> JuliaInfo(x, y, verifier(x, y)) }
                            .filter { ji -> !ji.converges }
                }.collect(Collectors.toList())
    }

}