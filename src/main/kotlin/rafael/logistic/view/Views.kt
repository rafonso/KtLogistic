package rafael.logistic.view

import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.util.StringConverter
import tornadofx.*


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

fun plotLines(coords: List<Pair<Double, Double>>, chartBackground: Node, handler: (Line, Int) -> Unit = { _, _ -> }) {
    (1 until coords.size)
            .map { i ->
                Line(coords[i - 1].first, coords[i - 1].second, coords[i].first, coords[i].second).also { l ->
                    handler(l, i)
                }
            }
            .forEach { l -> chartBackground.add(l) }
}