package rafael.logistic.view

import javafx.scene.paint.Color
import javafx.util.StringConverter


class SpinnerConverter(size: Int) : StringConverter<Double>() {

    private val format = "%.${size}f"

    override fun toString(value: Double?): String = if (value != null) format.format(value) else ""

    override fun fromString(string: String?): Double? = string?.toDoubleOrNull()

}

val CONVERTER_2 = SpinnerConverter(2) as StringConverter<Number>
val CONVERTER_0 = SpinnerConverter(0) as StringConverter<Number>

const val MIN_OPACITY = 0.3
const val MAX_H = 240.0

fun getStroke(x: Double): Color {
    val h = (1.0 - x) * MAX_H
    val o = MIN_OPACITY + (1.0 - MIN_OPACITY) * x

    return Color.hsb(h, 1.0, 0.5, o)
}

