package rafael.logistic.set

data class SetInfo(val col: Int, val row: Int, val x: Double, val y: Double, val iterationsToDiverge: Int?) {

    val converges = (iterationsToDiverge == null)

}

val emptyJuliaInfo = SetInfo(Int.MIN_VALUE, Int.MIN_VALUE, Double.NaN, Double.NaN, Int.MIN_VALUE)
