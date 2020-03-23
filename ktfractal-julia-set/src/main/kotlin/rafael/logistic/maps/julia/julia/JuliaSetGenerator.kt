package rafael.logistic.maps.julia.julia

import rafael.logistic.maps.julia.JuliaGenerator
import rafael.logistic.maps.julia.JuliaParameter

class JuliaSetGenerator : JuliaGenerator() {

    override fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int? =
            checkConvergence(x, y, parameter.cX, parameter.cY, interactions)

}