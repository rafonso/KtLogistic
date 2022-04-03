package rafael.logistic.set.mandelbrot

import rafael.logistic.set.SetGenerator
import rafael.logistic.set.SetParameter

class MandelbrotSetSwitchGenerator : SetGenerator() {

    private val ktGenerator = MandelbrotSetGenerator()

    private val nativeGenerator = MandelbrotSetNativeGenerator()

    private var useNative = false

    override fun verify(x: Double, y: Double, parameter: SetParameter?, interactions: Int): Int =
        if(useNative) {
            useNative = !useNative
            nativeGenerator.verify(x, y, parameter, interactions)
        } else {
            useNative = !useNative
            ktGenerator.verify(x, y, parameter!!, interactions)
        }

    override fun nextX(zx: Double, zy: Double, cx: Double, cy: Double): Double {
        TODO("Not yet implemented")
    }

    override fun nextY(zx: Double, zy: Double, cx: Double, cy: Double): Double {
        TODO("Not yet implemented")
    }
}