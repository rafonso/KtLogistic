package rafael.logistic.set.mandelbrot

import rafael.logistic.map.set.JuliaGenerator
import rafael.logistic.map.set.JuliaParameter

class MandelbrotSetGenerator : JuliaGenerator() {

    override fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int? =
            checkConvergence(0.0, 0.0, x, y, interactions)

}
