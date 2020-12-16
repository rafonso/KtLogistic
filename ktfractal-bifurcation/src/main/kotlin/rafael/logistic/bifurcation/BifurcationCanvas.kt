package rafael.logistic.bifurcation

import javafx.scene.image.PixelFormat
import rafael.logistic.core.fx.addBuffer
import rafael.logistic.core.fx.getRainbowColor
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.toBytes
import tornadofx.*

const val WHITE_BYTE = 0xFF.toByte()

class BifurcationCanvas : CanvasChart<RData, ByteArray>() {

    // @formatter:off

    val pixelsSeparationProperty    =   1.toProperty()

    private val pixelFormat         =   PixelFormat.getByteRgbInstance()

    private val colorCache          =   mutableMapOf<Double, ByteArray>()

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

    override fun dataToElementsToPlot(): Array<ByteArray> {
        val buffer = ByteArray(super.w * (super.h) * 4) { WHITE_BYTE }

        if (buffer.isNotEmpty()) {
            // Otimizações agressivas. Não precisa chamar os getters toda hora.
            val ym = yMin
            val deltaY = yMax - yMin
            val yToCanvas: (Double) -> Int = { y -> ((1 - (y - ym) / (deltaY)) * super.h).toInt() }
            val pixSep = pixelsSeparationProperty.value

            data.parallelStream()
                .flatMap { rSequenceToCoordinates(it, pixSep, yToCanvas).stream() }
                .filter { pi -> (0..super.h).contains(pi.yChart) }
                .forEach { pi ->
                    val pos = pi.xChart + pi.yChart * super.w

                    buffer.addBuffer(pos, pi.colorBuffer)
                }
        }

        return arrayOf(buffer)
    }

    override fun plotData(elements: Array<ByteArray>) {
        pixelWriter.setPixels(0, 0, super.w, super.h, pixelFormat, elements[0], 0, super.w * 3)
    }

}
