package rafael.logistic.maps.julia.mandelbrot

import rafael.logistic.maps.julia.JuliaGenerator
import rafael.logistic.maps.julia.JuliaParameter

class MandelbrotSetGenerator : JuliaGenerator() {

    override fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int? =
            checkConvergence(0.0, 0.0, x, y, interactions)

}