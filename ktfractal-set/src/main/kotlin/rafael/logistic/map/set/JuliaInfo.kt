package rafael.logistic.map.set

data class JuliaInfo(val col: Int, val row: Int, val x: Double, val y: Double, val iterationsToDiverge: Int?) {

    val converges = (iterationsToDiverge == null)

}

val emptyJuliaInfo = JuliaInfo(Int.MIN_VALUE, Int.MIN_VALUE, Double.NaN, Double.NaN, Int.MIN_VALUE)
