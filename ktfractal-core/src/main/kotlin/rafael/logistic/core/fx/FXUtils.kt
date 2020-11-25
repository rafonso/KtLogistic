package rafael.logistic.core.fx

import tornadofx.toProperty

/**
 * @return [javafx.beans.property.IntegerProperty] com valor inicial 1.
 */
fun oneProperty()     = 1    .toProperty()

/**
 * @return [javafx.beans.property.DoubleProperty] com valor inicial 0.1.
 */
fun decimalProperty() = (0.1).toProperty()
