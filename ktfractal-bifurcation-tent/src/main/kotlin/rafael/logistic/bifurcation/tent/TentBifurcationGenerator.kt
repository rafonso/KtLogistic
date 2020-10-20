package rafael.logistic.bifurcation.tent

import rafael.logistic.maps.bifurcation.BifurcationGenerator

const val MI_MIN = 0.0
const val MI_MAX = 2.0

const val X_MIN = 0.0
const val X_MAX = 1.0

// https://en.wikipedia.org/wiki/Tent_map
class TentBifurcationGenerator : BifurcationGenerator() {

    override fun getNextX(@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") mi: Double, x: Double): Double =
        if (x <= 0.5) mi * x else mi * (1 - x)

}
