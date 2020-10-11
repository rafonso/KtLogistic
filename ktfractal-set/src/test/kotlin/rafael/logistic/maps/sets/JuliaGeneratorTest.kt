package rafael.logistic.maps.sets

import rafael.logistic.core.generation.BiDouble

const val CYCLES = 1000

val values = arrayOf(1, 5, 10, 50, 100, 500, 1000)
val widthsHights = values.flatMap { w -> values.map { h -> Pair(w, h) } }.sortedBy { it.first * it.second }

object JuliaGeneratorStub : JuliaGenerator() {
    override fun verify(x: Double, y: Double, parameter: JuliaParameter, interactions: Int): Int? = 1
}

fun heat() {
    val parameter = JuliaParameter(0.0, 0.0, 0.0, 100.0, 1, 0.0, 100.0, 1)
    (1..100).forEach { _ -> JuliaGeneratorStub.generate(BiDouble.ZERO, parameter, 0) }
}

fun execute(width: Int, height: Int) {
    print("%4d\t%4d\t%7d\t".format(width, height, width * height))

    val parameter = JuliaParameter(0.0, 0.0, 0.0, 100.0, width, 0.0, 100.0, height)

    val t0 = System.currentTimeMillis()
    (1..CYCLES).forEach { _ -> JuliaGeneratorStub.generate(BiDouble.ZERO, parameter, 0) }
    val deltaT = System.currentTimeMillis() - t0

    println("%6d".format(deltaT))
}

fun main() {
    heat()

    widthsHights.forEach { (w, h) -> execute(w, h) }
}
