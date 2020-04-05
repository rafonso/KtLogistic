package rafael.logistic.maps.hiperbolic_tangent.data

import rafael.logistic.maps.bifurcation.BifurcationGenerator
import kotlin.math.tanh

const val G_MIN = 0.0
const val G_MAX = 20.0
const val X_MIN = 0.0
const val X_MAX = 6.0

// https://en.wikipedia.org/wiki/Logistic_map#Feigenbaum_universality_of_1-D_maps
class HiperbolicTangentGenerator : BifurcationGenerator() {

    override fun getNextX(r: Double, x: Double): Double = r * x * (1.0 - tanh(x))

}
