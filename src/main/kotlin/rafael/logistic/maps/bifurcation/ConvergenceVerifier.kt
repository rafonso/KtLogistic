package rafael.logistic.maps.bifurcation

import kotlin.math.abs

private const val CONVERGENCE_DIFF = 0.001

sealed class ConvergenceVerifier {

    companion object {

        fun valueOf(convergenceType: ConvergenceType, r: Double) = when (convergenceType) {
            ConvergenceType.ZERO     -> ZeroVerifier
            ConvergenceType.CONSTANT -> ConstantVerifier(r)
            ConvergenceType.CYCLE_2  -> Cycle2Verifier
            ConvergenceType.CHAOS    -> ChaosVerifier
        }

    }

    abstract fun converges(values: Data): Boolean

}

object ZeroVerifier : ConvergenceVerifier() {
    override fun converges(values: Data): Boolean = values.last() <= CONVERGENCE_DIFF
}

class ConstantVerifier(r: Double) : ConvergenceVerifier() {

    private val convergenceValue = (r - 1) / r

    override fun converges(values: Data): Boolean = abs(values.last() - convergenceValue) <= CONVERGENCE_DIFF

}

object Cycle2Verifier : ConvergenceVerifier() {
    override fun converges(values: Data): Boolean = (values.size > 2) &&
            (abs(values.last() - values[values.lastIndex - 2]) <= CONVERGENCE_DIFF)
}

object ChaosVerifier : ConvergenceVerifier() {
    override fun converges(values: Data): Boolean = false
}
