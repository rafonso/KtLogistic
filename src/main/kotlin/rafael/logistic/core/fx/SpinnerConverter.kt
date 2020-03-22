package rafael.logistic.core.fx

import javafx.util.StringConverter

class SpinnerConverter(size: Int) : StringConverter<Double>() {

    private val format = "%.${size}f"

    override fun toString(value: Double?): String = if (value != null) format.format(value) else ""

    override fun fromString(string: String?): Double? = string?.toDoubleOrNull()

}

val CONVERTER_2 = SpinnerConverter(2) as StringConverter<Number>
val CONVERTER_0 = SpinnerConverter(0) as StringConverter<Number>
