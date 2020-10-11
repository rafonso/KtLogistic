package rafael.logistic.maps.gingerbreadman

import rafael.logistic.core.generation.BiDouble

fun main() {
    val p0 = BiDouble(8.0, 0.5)
    val iterations = 1000
    val cycles = 5000
    val generator = GingerbreadmanGenerator()

    // Heating
    repeat(100) {
        generator.generate(p0, iterations)
    }

    val t0 = System.currentTimeMillis()
    repeat(cycles) {
        generator.generate(p0, iterations)
    }
    val deltaT = System.currentTimeMillis() - t0

    println(deltaT)
}
