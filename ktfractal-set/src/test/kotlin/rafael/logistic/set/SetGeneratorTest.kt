package rafael.logistic.set

import rafael.logistic.core.generation.BiDouble

const val CYCLES = 1000

val values =arrayOf(100, 200, 300, 400, 500, 550, 600)
//    arrayOf(1, 5, 10, 50, 100, 500, 1000)
val widthsHights = values.flatMap { w -> values.map { h -> Pair(w, h) } }.sortedBy { it.first * it.second }

object SetGeneratorStub : SetGenerator() {

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun nextX(zx: Double, zy: Double, cx: Double, cy: Double) = zx * zx - zy * zy + cx

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun nextY(zx: Double, zy: Double, cx: Double, cy: Double) = 2 * zx * zy + cy

    override fun verify(x: Double, y: Double, parameter: SetParameter, interactions: Int): Int = 1

}

fun heat(w: Int, h: Int) {
    println("Heating for $w x $h = ${w * h}")
    val parameter = SetParameter(0.0, 0.0, 0.0, 100.0, w, 0.0, 100.0, h)
    (1..100).forEach { _ -> SetGeneratorStub.generate(BiDouble.ZERO, parameter, 0) }
}

fun execute(width: Int, height: Int) {
    print("%4d\t%4d\t%7d\t".format(width, height, width * height))

    val parameter = SetParameter(0.0, 0.0, 0.0, 100.0, width, 0.0, 100.0, height)

    val t0 = System.currentTimeMillis()
    repeat(CYCLES) { SetGeneratorStub.generate(BiDouble.ZERO, parameter, 0) }
    val deltaT = System.currentTimeMillis() - t0

    println("%6d".format(deltaT))
}

fun main() {
    heat(10, 10)
    heat(101, 101)

    println("Running ...")

    widthsHights.forEach { (w, h) -> execute(w, h) }
}
