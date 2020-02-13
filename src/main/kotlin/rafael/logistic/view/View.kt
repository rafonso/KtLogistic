package rafael.logistic.view

import javafx.scene.paint.Color
import javafx.util.StringConverter


class SpinnerConverter(size: Int) : StringConverter<Double>() {

    private val format = "%.${size}f"

    override fun toString(value: Double?): String = if (value != null) format.format(value) else ""

    override fun fromString(string: String?): Double? = string?.toDoubleOrNull()

}

fun getStroke(x: Double): Color {
    val h = (1.0 - x) * 240
    val o = 0.4 + 0.6 * x

    return Color.hsb(h, 1.0, 0.5, o)
}

