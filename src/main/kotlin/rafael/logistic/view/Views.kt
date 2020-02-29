package rafael.logistic.view

import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.util.StringConverter
import tornadofx.*
import java.util.stream.Collectors


class SpinnerConverter(size: Int) : StringConverter<Double>() {

    private val format = "%.${size}f"

    override fun toString(value: Double?): String = if (value != null) format.format(value) else ""

    override fun fromString(string: String?): Double? = string?.toDoubleOrNull()

}

val CONVERTER_2 = SpinnerConverter(2) as StringConverter<Number>
val CONVERTER_0 = SpinnerConverter(0) as StringConverter<Number>

const val MIN_OPACITY = 0.5

const val DELTA_COLOR = 1.0 / 6
val rainbow = arrayOf(Color.VIOLET, Color.INDIGO, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED)

private fun getBaseColors(x: Double): Int =
        when {
            // @formatter:off
            x < 1 * DELTA_COLOR -> 1
            x < 2 * DELTA_COLOR -> 2
            x < 3 * DELTA_COLOR -> 3
            x < 4 * DELTA_COLOR -> 4
            x < 5 * DELTA_COLOR -> 5
            else                -> 6
            // @formatter:on
        }

fun getRainbowColor(x: Double): Color {
    val pos = getBaseColors(x)
    val c0 = rainbow[pos - 1]
    val c1 = rainbow[pos]
    val delta = x / DELTA_COLOR - pos + 1

    val r = (c1.red   - c0.red  ) * delta + c0.red
    val g = (c1.green - c0.green) * delta + c0.green
    val b = (c1.blue  - c0.blue ) * delta + c0.blue
    val o = (1.0 - x) * MIN_OPACITY + x

    return Color(r, g, b, o)
}

fun plotLines(coords: List<Pair<Double, Double>>, chartBackground: Node, handler: (Line, Int) -> Unit = { _, _ -> }) {
    val elements = (1 until coords.size)
            .toList()
            .parallelStream()
            .map { i ->
                Line(coords[i - 1].first, coords[i - 1].second, coords[i].first, coords[i].second).also { l ->
                    handler(l, i)
                }
            }
            .collect(Collectors.toList())

    chartBackground.getChildList()?.addAll(elements)
}
