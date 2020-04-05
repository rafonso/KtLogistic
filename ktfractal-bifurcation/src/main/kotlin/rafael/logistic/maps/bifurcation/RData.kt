package rafael.logistic.maps.bifurcation

typealias Data = List<Double>

const val R_MIN = 0.0
const val R_MAX = 4.0
const val X_MIN = 0.0
const val X_MAX = 1.0

data class RData(val col: Int, val r: Double, val values: Data)
