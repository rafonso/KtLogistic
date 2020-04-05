package rafael.logistic.maps.bifurcation

const val R_MIN = 0.0
const val R_MAX = 4.0
const val X_MIN = 0.0
const val X_MAX = 1.0

class LogisticBifurcationGenerator: BifurcationGenerator() {

    override fun getNextX(r: Double, x: Double): Double = r * x * (1.0 - x)

}
