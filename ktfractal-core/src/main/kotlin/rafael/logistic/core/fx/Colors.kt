package rafael.logistic.core.fx

import javafx.scene.paint.Color

/**
 * Funções auxiliares de cores
 */

private const val MIN_OPACITY = 0.5

const val DELTA_COLOR = 1.0 / 6

val rainbow =
    arrayOf(Color.VIOLET, Color.INDIGO, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED)

private val colorRange = (1..6).toList().toIntArray()

private fun media(min: Double, max: Double, factor: Double) = (max - min) * factor + min

fun getRainbowColor(x: Double): Color {
    val pos = colorRange.first { x <= it * DELTA_COLOR }
    val c0 = rainbow[pos - 1]
    val c1 = rainbow[pos]
    val delta = x / DELTA_COLOR - pos + 1

    val r = media(c0.red, c1.red, delta)
    val g = media(c0.green, c1.green, delta)
    val b = media(c0.blue, c1.blue, delta)
    val o = media(x, 1.0, MIN_OPACITY)

    return Color(r, g, b, o)
}

/**
 * [Array de 3 bytes][ByteArray] representando a [Cor][Color] [Preta][Color.BLACK].
 */
val blackBuffer = Color.BLACK.toBytes()

/**
 * Converte uma [Cor][Color] num [array de 3 bytes][ByteArray] representando os componentes dela.
 *
 * @return Array de 3 bytes representando a cor.
 */
fun Color.toBytes() = ByteArray(3) {
    // TODO Adicionar transparencia, Tem que retornar IntArray
    (when (it) {
        0 -> this.red
        1 -> this.green
        2 -> this.blue
        else -> error("it: $it, Cor: $this")
    } * 255).toInt().toByte()
}

/**
 *
 */
fun ByteArray.addBuffer(i: Int, bc: ByteArray): ByteArray {
    this[i * 3 + 0] = bc[0]
    this[i * 3 + 1] = bc[1]
    this[i * 3 + 2] = bc[2]

    return this
}
