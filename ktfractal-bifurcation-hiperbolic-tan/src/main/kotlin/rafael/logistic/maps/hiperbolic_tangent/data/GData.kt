package rafael.logistic.maps.hiperbolic_tangent.data

typealias Data = List<Double>

const val G_MIN = 0.0
const val G_MAX = 20.0
const val X_MIN = 0.0
const val X_MAX = 6.0

data class GData(val col: Int, val g: Double, val values: Data)
