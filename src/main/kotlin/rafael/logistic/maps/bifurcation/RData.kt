package rafael.logistic.maps.bifurcation

typealias Data = List<Double>

data class RData(val r: Double, val values: Data, val convergenceType: ConvergenceType?) {

    constructor(r: Double, x0: Double) : this(r, listOf(x0), null)

}