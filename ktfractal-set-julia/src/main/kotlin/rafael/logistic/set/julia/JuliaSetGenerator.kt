package rafael.logistic.set.julia

import rafael.logistic.set.SetGenerator
import rafael.logistic.set.SetParameter

class JuliaSetGenerator : SetGenerator() {

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun nextX(zx: Double, zy: Double, cx: Double, cy: Double) = zx * zx - zy * zy + cx

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun nextY(zx: Double, zy: Double, cx: Double, cy: Double) = 2 * zx * zy + cy

    override fun verify(x: Double, y: Double, parameter: SetParameter, interactions: Int): Int =
            checkConvergence(x, y, parameter.cX, parameter.cY, interactions)

}
