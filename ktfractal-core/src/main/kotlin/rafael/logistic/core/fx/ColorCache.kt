package rafael.logistic.core.fx

import javafx.scene.paint.Color
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.round

private const val MIN_OPACITY = 0.5

/**
 * Representa as cores do arco-íris, indicando a intensidade ou "calor", do mais "frio" ([Color.VIOLET]) ao
 * mais quente ([Color.RED])
 */
val rainbowColors =
    arrayOf(Color.VIOLET, Color.INDIGO, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED)

sealed class BaseColorCache<T> constructor(private val colors: Array<out Color>) {

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

    /**
     * Retorna o equivalente da cor equivalente a um valor.
     *
     * @param x valor de `0.0` a `1.0`
     * @return Equivalente da cor ao valor passado.
     */
    abstract operator fun get(x: Double): T

    /**
     * Retorna o equivalente da cor equivalente a um valor.
     *
     * @param i Numerador
     * @param max Denominador
     * @return Equivalente da cor a divisão `i / max`
     */
    fun get(i: Int, max: Int): T = get(i.toDouble() / max)
}

/**
 * Representa um cache de [cores][Color], onde um número da `0.0` a `1.0` é usado para determinar a cor dentro de um
 * [Array]
 *
 * @constructor
 * @param colors [Array] de [cores][Color]
 */
class ColorCache(colors: Array<out Color>) : BaseColorCache<Color>(colors) {

    private val colorCache = ConcurrentHashMap<Double, Color>()

    override operator fun get(x: Double): Color = colorCache.getOrPut(x) { calculateColor(x) }

}

/**
 * Representa um cache de [cores][Color], onde cada uma é mapeada a um [Inteiro][Int].
 * A fórmula de mapeamento pode ser encontrada em [https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/PixelFormat.html#getByteBgraInstance--]
 *
 * @constructor
 * @param colors [Array] de [cores][Color]
 */
class ColorIntCache(colors: Array<out Color>) : BaseColorCache<Int>(colors) {

    companion object {

        private fun colorToInt(c: Color): Int {
            val a = round(255 * c.opacity).toInt()
            val r = round(255 * c.red).toInt()
            val g = round(255 * c.green).toInt()
            val b = round(255 * c.blue).toInt()

            return (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        /**
         * [Int] representando a [Cor][Color] [Preta][Color.BLACK].
         */
        val black = colorToInt(Color.BLACK)

        /**
         * [Int] representando a [Cor][Color] [Branca][Color.WHITE].
         */
        val white = colorToInt(Color.WHITE)

    }

    /*
     * Usando ConcurrentHashMap no lugar de HashMap para evitar problema de ClassCastException:
     * java.util.HashMap$Node cannot be cast to java.util.HashMap$TreeNode
     *
     * Ver https://stackoverflow.com/questions/29967401/strange-hashmap-exception-hashmapnode-cannot-be-cast-to-hashmaptreenode/29971168
     */
    private val intsCache = ConcurrentHashMap<Double, Int>()

    override operator fun get(x: Double): Int = try {
        intsCache.getOrPut(x) { colorToInt(calculateColor(x)) }
    } catch (e: Exception) {
        throw Exception("get($x): ${e.message}", e)
    }

}
