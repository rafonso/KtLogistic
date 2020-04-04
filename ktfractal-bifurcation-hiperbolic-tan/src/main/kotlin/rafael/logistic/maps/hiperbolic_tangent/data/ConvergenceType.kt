package rafael.logistic.maps.hiperbolic_tangent.data

const val END_CYCLE_2 = 8.0
const val END_VALUE = 17.000000000001 // A small delta to deal with rounding problems

enum class ConvergenceType {
    CYCLE_2, CHAOS;

    companion object {

        fun valueOf(g: Double) =
                when (g) {
                    in 0.0..END_CYCLE_2 -> CYCLE_2
                    in END_CYCLE_2..END_VALUE -> CHAOS
                    else                      -> error("Invalid G: $g")
                }

    }
}
