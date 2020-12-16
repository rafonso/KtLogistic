package rafael.logistic.bifurcation

import rafael.logistic.core.fx.addBuffer
import rafael.logistic.core.fx.getRainbowColor
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.toBytes
import tornadofx.*

const val WHITE_BYTE = 0xFF.toByte()

class BifurcationCanvas : CanvasChart<RData>() {

    // @formatter:off

            val pixelsSeparationProperty    =   1.toProperty()

    private val colorCache                  =   mutableMapOf<Double, ByteArray>()

    // @formatter:on

    private fun rSequenceToCoordinates(
        rSequence: RData,
        pixSep: Int,
        yToCanvas: (Double) -> Int
    ): Collection<PixelInfo> {
        val size = rSequence.values.size
        val rPos = rSequence.col * pixSep

        return rSequence.values.reversed()
            .mapIndexed { i, v ->
                val dblColor = (size - i).toDouble() / size
                val buffColor = colorCache.getOrPut(dblColor) {
                    getRainbowColor(dblColor).toBytes()
                }

                PixelInfo(rPos, yToCanvas(v), buffColor)
            }
            .toSet()
    }

    override fun dataToElementsToPlot(): ByteArray {
        val h = super.h
        val w = super.w
        val buffer = ByteArray(w * h * 4) { WHITE_BYTE }

        if (buffer.isNotEmpty()) {
            // Otimizações agressivas. Não precisa chamar os getters toda hora.
            val ym = yMin
            val deltaY = yMax - yMin
            val yToCanvas: (Double) -> Int = { y -> ((1 - (y - ym) / (deltaY)) * h).toInt() }
            val pixSep = pixelsSeparationProperty.value

            data.parallelStream()
                .flatMap { rSequenceToCoordinates(it, pixSep, yToCanvas).stream() }
                .filter { pi -> (0..h).contains(pi.yChart) }
                .forEach { pi ->
                    val pos = pi.xChart + pi.yChart * w

                    buffer.addBuffer(pos, pi.colorBuffer)
                }
        }

        return buffer
    }

}
