package rafael.logistic.maps.bifurcation

import kotlin.math.abs

private const val CONVERGENCE_DIFF = 0.001

sealed class ConvergenceVerifier {

    abstract fun converges(values: Data): Boolean

}

object ZeroVerifier : ConvergenceVerifier() {
    override fun converges(values: Data): Boolean = values.last() <= CONVERGENCE_DIFF
}

class ConstanstVerifier(r: Double) : ConvergenceVerifier() {

    private val convergenceValue = (r - 1) / r

    override fun converges(values: Data): Boolean = abs(values.last() - convergenceValue) <= CONVERGENCE_DIFF

}

object Cycle2Verifier: ConvergenceVerifier() {
    override fun converges(values: Data): Boolean = (values.size > 2) &&
            (abs(values.last() - values[values.lastIndex - 2]) <= CONVERGENCE_DIFF)
}

object ChaosVerifier: ConvergenceVerifier() {
    override fun converges(values: Data): Boolean = false
}