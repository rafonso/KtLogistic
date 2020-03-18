package rafael.logistic.maps.julia

data class JuliaInfo(val x: Double, val y: Double, val iterationsToDiverge: Int?) {

    val converges = (iterationsToDiverge == null)

}