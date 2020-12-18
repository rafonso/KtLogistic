package rafael.logistic.bifurcation

import rafael.logistic.core.fx.addBuffer
import rafael.logistic.core.fx.getRainbowColor
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.toBytes
import tornadofx.*

const val WHITE_BYTE = 0xFF.toByte()

class BifurcationCanvas : CanvasChart<RData>() {

    // @formatter:off

            val pixelsSeparationProperty    =   1.toProperty()

            val iterationsProperty          =   oneProperty()

    private val cachePosByIterations        =   mutableMapOf<Int, DoubleArray>()

    private var cachePos                    :   DoubleArray = DoubleArray(0)

    private val colorCache                  =   mutableMapOf<Double, ByteArray>()

    // @formatter:on

    init {
        super.initialize()

        iterationsProperty.onChange { iterations ->
            cachePos = cachePosByIterations.getOrPut(iterations) {
                (0..iterations).map { it.toDouble() / iterations }.toDoubleArray()
            }
        }
    }

    private fun pixelInfo(i: Int, v: Double, rPos: Int, yToCanvas: (Double) -> Int): PixelInfo {
        val dblColor = cachePos[i]
        val buffColor = colorCache.getOrPut(dblColor) {
            getRainbowColor(dblColor).toBytes()
        }

        return PixelInfo(rPos, yToCanvas(v), buffColor)
    }

    private fun rSequenceToCoordinates(
        rSequence: RData,
        pixSep: Int,
        yToCanvas: (Double) -> Int
    ): Collection<PixelInfo> {
        val rPos = rSequence.col * pixSep

        return rSequence.values
            .mapIndexed { i, v ->
                pixelInfo(i, v, rPos, yToCanvas)
            }
    }

    override fun dataToElementsToPlot(data0: List<RData>): ByteArray {
        val h = super.h
        val w = super.w
        val buffer = ByteArray(w * h * 4) { WHITE_BYTE }

        if (buffer.isNotEmpty()) {
            // Otimizações agressivas. Não precisa chamar os getters toda hora.
            val ym = yMin
            val deltaY = yMax - yMin
            val yToCanvas: (Double) -> Int = { y -> ((1 - (y - ym) / (deltaY)) * h).toInt() }
            val pixSep = pixelsSeparationProperty.value

            data0
                .parallelStream()
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
