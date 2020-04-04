package rafael.logistic.maps.hiperbolic_tangent.data

import kotlin.math.abs

private const val CONVERGENCE_DIFF = 0.001

sealed class ConvergenceVerifier {

    companion object {

        fun valueOf(convergenceType: ConvergenceType, g: Double) = when (convergenceType) {
            ConvergenceType.CYCLE_2  -> Cycle2Verifier
            ConvergenceType.CHAOS    -> ChaosVerifier
        }

    }

    abstract fun converges(values: Data): Boolean

}

object Cycle2Verifier : ConvergenceVerifier() {
    override fun converges(values: Data): Boolean = (values.size > 2) &&
            (abs(values.last() - values[values.lastIndex - 2]) <= CONVERGENCE_DIFF)
}

object ChaosVerifier : ConvergenceVerifier() {
    override fun converges(values: Data): Boolean = false
}
