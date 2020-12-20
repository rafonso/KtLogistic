package rafael.logistic.core.fx

import javafx.scene.paint.Color

private const val MIN_OPACITY = 0.5

/**
 * Representa as cores do arco-íris, indicando a intensidade ou "calor", do mais "frio" ([Color.VIOLET]) ao
 * mais quente ([Color.RED])
 */
val rainbowColors =
    arrayOf(Color.VIOLET, Color.INDIGO, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED)

sealed class BaseColorCache constructor(private val colors: Array<out Color>) {

    private val delta = 1.0 / colors.lastIndex

    private val range = (1..colors.lastIndex).toList().toIntArray()

    private fun media(min: Double, max: Double, factor: Double) = (max - min) * factor + min

    protected fun calculateColor(x: Double): Color {
        val pos = range.first { x <= it * delta }
        val c0 = colors[pos - 1]
        val c1 = colors[pos]
        val delta = x / delta - pos + 1

        val r = media(c0.red, c1.red, delta)
        val g = media(c0.green, c1.green, delta)
        val b = media(c0.blue, c1.blue, delta)
        val o = media(x, 1.0, MIN_OPACITY)

        return Color(r, g, b, o)
    }

}

/**
 * Representa um cache de [cores][Color], onde um número da `0.0` a `1.0` é usado para determinar a cor dentro de um
 * [Array]
 *
 * @constructor
 * @param colors [Array] de [cores][Color]
 */
class ColorCache(colors: Array<out Color>) : BaseColorCache(colors) {

    companion object {
//        fun toCache(vararg colors: Color) = ColorCache(colors)
    }

    private val colorCache = mutableMapOf<Double, Color>()

    /**
     * Retorna a cor equivalente de um valor.
     *
     * @param x valor de `0.0` a `1.0`
     * @return cor equivalente
     */
    fun getColor(x: Double) = colorCache.getOrPut(x) { calculateColor(x) }

    /**
     * Retorna a cor equivalente de um valor.
     *
     * @param i Numerador
     * @param max Denominador
     * @return cor equivalente a `i / max`
     */
    fun getColor(i: Int, max: Int) = getColor(i.toDouble() / max)

}

/**
 * Representa um cache de [cores][Color], onde um número da `0.0` a `1.0` é usado para determinar um [ByteArray]
 * equivalente a cor dentro de um [Array]
 *
 * @constructor
 * @param colors [Array] de [cores][Color]
 */
class ColorBytesCache(colors: Array<out Color>) : BaseColorCache(colors) {

    companion object {

//        fun toCache(vararg colors: Color) = ColorBytesCache(colors)

        /**
         * [Array de 3 bytes][ByteArray] representando a [Cor][Color] [Preta][Color.BLACK].
         */
        val blackBuffer = byteArrayOf(0, 0, 0)

    }

    private val bytesCache = hashMapOf<Double, ByteArray>()

    private fun toBytes(c: Color) = ByteArray(3) {
        // TODO Adicionar transparencia, Tem que retornar IntArray
        (when (it) {
            0 -> c.red
            1 -> c.green
            2 -> c.blue
            else -> error("it: $it, Cor: $this")
        } * 255).toInt().toByte()
    }

    /**
     * Retorna o [ByteArray] da cor equivalente de um valor.
     *
     * @param x valor de `0.0` a `1.0`
     * @return [ByteArray] da cor equivalente
     */
    fun getBytes(x: Double) = try {
        bytesCache.getOrPut(x) { toBytes(calculateColor(x)) }
    } catch (e: Exception) {
        throw Exception("getBytes($x)", e)
    }

    /**
     * Retorna o [ByteArray] da cor equivalente de um valor.
     *
     * @param i Numerador
     * @param max Denominador
     * @return [ByteArray] da cor equivalente a `i / max`
     */
    fun getBytes(i: Int, max: Int) = getBytes(i.toDouble() / max)

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
