package rafael.logistic.view

import javafx.scene.paint.Color

/**
 * Funções auxiliares de cores
 */

private const val MIN_OPACITY = 0.5

const val DELTA_COLOR = 1.0 / 6

val rainbow = arrayOf(Color.VIOLET, Color.INDIGO, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED)

private fun media(min: Double, max: Double, factor: Double) = (max - min) * factor + min

fun getRainbowColor(x: Double): Color {
    val pos = (1..6).first { x <= it * DELTA_COLOR }
    val c0 = rainbow[pos - 1]
    val c1 = rainbow[pos]
    val delta = x / DELTA_COLOR - pos + 1

    val r = media(c0.red, c1.red, delta)
    val g = media(c0.green, c1.green, delta)
    val b = media(c0.blue, c1.blue, delta)
    val o = media(x, 1.0, MIN_OPACITY)

    return Color(r, g, b, o)
}

