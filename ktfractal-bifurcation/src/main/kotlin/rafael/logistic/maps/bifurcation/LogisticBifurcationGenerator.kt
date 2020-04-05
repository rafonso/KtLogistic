package rafael.logistic.maps.bifurcation

class LogisticBifurcationGenerator: BifurcationGenerator() {

    override fun getNextX(r: Double, x: Double): Double = r * x * (1.0 - x)

}
