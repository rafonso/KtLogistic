package rafael.logistic.core.fx

const val MAX_TESTAR = 10_000_000
const val MAX_RUNS = 10
const val MAX_RANGE = 10
val pos = doubleArrayOf(
    0.0,
    0.1,
    1.0,
    1.0 * MAX_RANGE / 3.0,
    MAX_RANGE / 2.0,
    2.0 * MAX_RANGE / 3.0,
    MAX_RANGE - 1.0,
    MAX_RANGE - 0.1,
    MAX_RANGE.toDouble()
)

fun testar(x: Double, acessor: (Double) -> Int): Long {
    val t0 = System.currentTimeMillis()
    (1..MAX_TESTAR).forEach { _ -> acessor(x) }
    val t1 = System.currentTimeMillis()

    return (t1 - t0)
}

private fun testar0(descricao: String, acessor: (Double) -> Int) {
    println()
    // Aquecimento
    (1..10).forEach { _ -> testar(MAX_RANGE / 4.0, acessor) }
    (1..MAX_RUNS).forEach { _ ->
        println(pos.map { value -> testar(value, acessor) }.joinToString("\t", prefix = "$descricao\t"))
    }
}

private fun testarArray() {
    // No Java Flight Recorder, esse método gasta ~ 44,5% to tempo de execução
    val array = (1..MAX_RANGE).toList().toIntArray()
    val acessor: (Double) -> Int = { x -> array.first { x <= it } }
    testar0("ARRAY", acessor)
}

private fun testarRange() {
    // No Java Flight Recorder, esse método gasta ~ 55,5% to tempo de execução
    val range = (1..MAX_RANGE) //10)
    val acessor: (Double) -> Int = { x -> range.first { x <= it } }
    testar0("RANGE", acessor)
}

fun main() {
    println(pos.joinToString("\t", prefix = "VALUE\t") { "%2.1f".format(it) })
    testarRange()
    testarArray()
}

