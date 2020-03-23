package rafael.logistic.maps.sets

data class JuliaInfo(val col: Int, val row: Int, val x: Double, val y: Double, val iterationsToDiverge: Int?) {

    val converges = (iterationsToDiverge == null)

}
