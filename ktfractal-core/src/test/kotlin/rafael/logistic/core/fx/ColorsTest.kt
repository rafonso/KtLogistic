package rafael.logistic.core.fx

const val MAX = 100_000_000

fun main() {
    val t0 = System.currentTimeMillis()
    (1..MAX).forEach { i ->
        getRainbowColor(Math.random())
    }
    val deltaT = System.currentTimeMillis() - t0

    println(deltaT)
}
