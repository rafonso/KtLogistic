package rafael.logistic.sets.mandelbrot

import rafael.logistic.maps.sets.JuliaGenerator
import rafael.logistic.maps.sets.JuliaParameter

class MandelbrotSetGenerator : JuliaGenerator() {

    override fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int? =
            checkConvergence(0.0, 0.0, x, y, interactions)

}
