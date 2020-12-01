package rafael.logistic.set.mandelbrot

import rafael.logistic.set.SetGenerator
import rafael.logistic.set.SetParameter

class MandelbrotSetGenerator : SetGenerator() {

    override fun verify(x: Double, y: Double, parameter: SetParameter, interactions: Int): Int? =
            checkConvergence(0.0, 0.0, x, y, interactions)

}
