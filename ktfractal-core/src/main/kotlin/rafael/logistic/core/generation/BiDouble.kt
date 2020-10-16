package rafael.logistic.core.generation

/**
 * Representação de um valor bidimensional.
 */
data class BiDouble(val x: Double, val y: Double) {

    companion object {
        /**
         * [BiDouble] composto por dois 0s
         */
        val ZERO = BiDouble(0.0, 0.0)

        /**
         * [BiDouble] composto por dois [Double.NaN]
         */
        val NAN = BiDouble(Double.NaN, Double.NaN)
    }

    override fun toString(): String = "($x, $y)"

}

