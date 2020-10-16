package rafael.logistic.core.fx

import javafx.util.StringConverter

/**
 * Spinner converter
 *
 * @constructor
 *
 * @param size
 */
internal class SpinnerConverter(size: Int) : StringConverter<Double>() {

    private val format = "%.${size}f"

    override fun toString(value: Double?): String = if (value != null) format.format(value) else ""

    override fun fromString(string: String?): Double? = string?.toDoubleOrNull()

}

@Suppress("UNCHECKED_CAST")
internal val CONVERTER_2 = SpinnerConverter(2) as StringConverter<Number>
