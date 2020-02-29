package rafael.logistic.view

import javafx.scene.paint.Color

private const val MIN_OPACITY = 0.5

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

    val r = (c1.red - c0.red) * delta + c0.red
    val g = (c1.green - c0.green) * delta + c0.green
    val b = (c1.blue - c0.blue) * delta + c0.blue
    val o = (1.0 - x) * MIN_OPACITY + x

    return Color(r, g, b, o)
}
