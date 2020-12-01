package rafael.logistic.set.julia

import rafael.logistic.set.SetGenerator
import rafael.logistic.set.SetParameter

class JuliaSetGenerator : SetGenerator() {

    override fun verify(x: Double, y: Double, parameter: SetParameter, interactions: Int): Int? =
            checkConvergence(x, y, parameter.cX, parameter.cY, interactions)

}
