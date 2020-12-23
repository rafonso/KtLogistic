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
 * Representa um cache de [cores][Color], onde cada uma é mapeada a um [Inteiro][Int].
 * A fórmula de mapeamento pode ser encontrada em [https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/PixelFormat.html#getByteBgraInstance--]
 *
 * @constructor
 * @param colors [Array] de [cores][Color]
 */
class ColorIntCache(colors: Array<out Color>) : BaseColorCache(colors) {

    /**
     * [Int] representando a [Cor][Color] [Preta][Color.BLACK].
     */
    val blackBuffer = colorToInt(Color.BLACK)

    val whiteBuffer = colorToInt(Color.WHITE)

    /*
     * Usando ConcurrentHashMap no lugar de HashMap para evitar problema de ClassCastException:
     * java.util.HashMap$Node cannot be cast to java.util.HashMap$TreeNode
     *
     * Ver https://stackoverflow.com/questions/29967401/strange-hashmap-exception-hashmapnode-cannot-be-cast-to-hashmaptreenode/29971168
     */
    private val bytesCache = ConcurrentHashMap<Double, Int>()

    private fun colorToInt(c: Color): Int {
        val a = round(c.opacity * 255).toInt()
        val r = round(c.red * 255).toInt()
        val g = round(c.green * 255).toInt()
        val b = round(c.blue * 255).toInt()

        return (a shl 24) or (r shl 16) or (g shl 8) or b
    }

    /**
     * Retorna o [ByteArray] da cor equivalente de um valor.
     *
     * @param x valor de `0.0` a `1.0`
     * @return [ByteArray] da cor equivalente
     */
    fun getInts(x: Double): Int = try {
        bytesCache.getOrPut(x) { colorToInt(calculateColor(x)) }
    } catch (e: Exception) {
        throw Exception("getBytes($x): ${e.message}", e)
    }

    /**
     * Retorna o [ByteArray] da cor equivalente de um valor.
     *
     * @param i Numerador
     * @param max Denominador
     * @return [ByteArray] da cor equivalente a `i / max`
     */
    fun getInts(i: Int, max: Int): Int = getInts(i.toDouble() / max)

}
