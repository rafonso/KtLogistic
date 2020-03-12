package rafael.logistic.maps.bifurcation

const val CHAOS_BORDER = 3.449489742783178 // = 1.0 + sqrt(6)

enum class ConvergenceType {
    ZERO, CONSTANT, CYCLE_2, CHAOS;

    companion object {

        fun valueOf(r: Double) =
                when (r) {
                    in 0.0..1.0          -> ConvergenceType.ZERO
                    in 1.0..3.0          -> ConvergenceType.CONSTANT
                    in 3.0..CHAOS_BORDER -> ConvergenceType.CYCLE_2
                    in CHAOS_BORDER..4.0 -> ConvergenceType.CHAOS
                    else                 -> error("Invalid r: $r")
                }

    }
}