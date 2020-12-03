package rafael.logistic.core.fx

import tornadofx.toProperty

/**
 * @return [javafx.beans.property.IntegerProperty] com valor inicial `1`.
 */
fun oneProperty()       = 1    .toProperty()

/**
 * @return [javafx.beans.property.DoubleProperty] com valor inicial `0.0`.
 */
fun zeroProperty()      = (0.0).toProperty()