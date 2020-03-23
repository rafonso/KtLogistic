package rafael.logistic.maps.bifurcation

const val END_CYCLE_2 = 3.449489742783178 // = 1.0 + sqrt(6)
const val END_VALUE = 4.000000000001 // A small delta to deal with rounding problems

enum class ConvergenceType {
    CYCLE_2, CHAOS;

    companion object {

        fun valueOf(r: Double) =
                when (r) {
                    in 0.0..END_CYCLE_2       -> CYCLE_2
                    in END_CYCLE_2..END_VALUE -> CHAOS
                    else                      -> error("Invalid r: $r")
                }

    }
}