package rafael.logistic.generator

data class BiPoint(val x: Double, val y: Double) {

    override fun toString(): String = "(%f, %f)".format(x, y)

}
