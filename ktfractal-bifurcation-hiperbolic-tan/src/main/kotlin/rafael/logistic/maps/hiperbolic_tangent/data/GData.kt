package rafael.logistic.maps.hiperbolic_tangent.data

typealias Data = List<Double>

data class GData(val col: Int, val g: Double, val values: Data, val convergenceType: ConvergenceType)
