package rafael.logistic.bifurcation

data class RData(val col: Int, val r: Double, val values: DoubleArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RData) return false

        if (col != other.col) return false
        if (r != other.r) return false
        return values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        var result = col
        result = 31 * result + r.hashCode()
        result = 31 * result + values.contentHashCode()
        return result
    }

}
