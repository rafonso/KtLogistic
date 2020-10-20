package rafael.logistic.set.julia

import rafael.logistic.map.set.JuliaGenerator
import rafael.logistic.map.set.JuliaParameter

class JuliaSetGenerator : JuliaGenerator() {

    override fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int? =
            checkConvergence(x, y, parameter.cX, parameter.cY, interactions)

}