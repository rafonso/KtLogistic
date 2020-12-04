package rafael.logistic.set.burning_ship

import rafael.logistic.set.SetGenerator
import rafael.logistic.set.SetParameter
import kotlin.math.abs

/**
 * https://en.wikipedia.org/wiki/Burning_Ship_fractal
 * http://paulbourke.net/fractals/burnship/
 * https://theory.org/fracdyn/burningship/
 */
class BurningShipSetGenerator : SetGenerator() {

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun nextX(zx: Double, zy: Double, cx: Double, cy: Double) =
        zx * zx - zy * zy - cx

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun nextY(zx: Double, zy: Double, cx: Double, cy: Double) =
        2 * abs(zx * zy) - cy

    override fun verify(x: Double, y: Double, parameter: SetParameter, interactions: Int): Int? =
        checkConvergence(0.0, 0.0, x, y, interactions)

}
