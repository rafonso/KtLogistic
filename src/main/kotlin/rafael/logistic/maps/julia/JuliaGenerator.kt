package rafael.logistic.maps.julia

import javafx.geometry.Point2D
import rafael.logistic.view.IterationGenerator
import rafael.logistic.view.IterationParameter
import java.util.stream.Collectors
import kotlin.math.abs

const val ESCAPE_RADIUS = 2.0

data class JuliaParameter(val c: Point2D,
                          val xMin: Double, val xMax: Double, val xScale: Int,
                          val yMin: Double, val yMax: Double, val yScale: Int) : IterationParameter {

    val deltaX = (xMax - xMin) / xScale

    val deltaY = (yMax - yMin) / yScale

    val xValues = (0..xScale).map { it * deltaX + xMin }

    val yValues = (0..yScale).map { it * deltaY + yMin }

}

//bean.isIsMandelbrot() ?
//checkConvergence(ci           , c             , 0.0   , 0.0   , bean.getConvergenceSteps()) :
//checkConvergence(bean.getZi() , bean.getZ()   , ci    , c     , bean.getConvergenceSteps())


class JuliaGenerator : IterationGenerator<Point2D, JuliaInfo, JuliaParameter> {

    private fun checkConvergence(ci: Double, c: Double, z: Double, zi: Double, convergenceSteps: Int): Int {
        var z = z
        var zi = zi
        for (i in 0 until convergenceSteps) {
            val ziT = 2 * (z * zi)
            val zT = z * z - zi * zi
            z = zT + c
            zi = ziT + ci
            if (z * z + zi * zi >= 4.0) {
                return i
            }
        }
        return convergenceSteps
    }


    override fun generate(z0: Point2D, parameter: JuliaParameter, interactions: Int): List<JuliaInfo> {

        fun isIgual(x: Double, y: Double) = (abs(x) in (abs(y) - parameter.deltaY)..(abs(y) + parameter.deltaY))
                && (abs(y) in (abs(x) - parameter.deltaX)..(abs(x) + parameter.deltaX))
        return parameter.xValues.parallelStream()
                .flatMap { x ->
                    parameter.yValues.parallelStream()
                            .map { y -> JuliaInfo(x, y, if (isIgual(x, y)) (interactions * Math.random()).toInt() else null) }
                            .filter { ji -> !ji.converges }
                }.collect(Collectors.toList())
    }

}