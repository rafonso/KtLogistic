package rafael.logistic.sets.julia

import rafael.logistic.maps.sets.JuliaGenerator
import rafael.logistic.maps.sets.JuliaParameter

class JuliaSetGenerator : JuliaGenerator() {

    override fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int? =
            checkConvergence(x, y, parameter.cX, parameter.cY, interactions)

}
