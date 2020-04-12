package rafael.logistic.core.generation

/**
 * Representação de um valor bidimensional.
 */
data class BiDouble(val x: Double, val y: Double) {

    companion object {
        val ZERO = BiDouble(0.0, 0.0)
        val NAN = BiDouble(Double.NaN, Double.NaN)
    }

    override fun toString(): String = "($x, $y)"

}

