package rafael.logistic.maps.bifurcation

data class RData(val r: Double, val values: List<Double>) {
    constructor(r: Double, x0: Double): this(r, listOf(x0))
}