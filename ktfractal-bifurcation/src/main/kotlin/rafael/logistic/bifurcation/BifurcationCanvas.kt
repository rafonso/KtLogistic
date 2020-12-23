package rafael.logistic.bifurcation

import rafael.logistic.core.fx.ColorIntCache
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.rainbowColors
import tornadofx.*

class BifurcationCanvas : CanvasChart<RData>() {

    // @formatter:off

            val pixelsSeparationProperty    =   oneProperty()

            val iterationsProperty          =   oneProperty()

            val firstIterationProperty      =   oneProperty()

    private val cachePosByIterations        =   mutableMapOf<Int, DoubleArray>()

    private var cachePos                    :   DoubleArray = DoubleArray(0)

    private val colorBytesCache             =   ColorIntCache(rainbowColors)

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
        val buffColor = colorBytesCache.getInts(dblColor)

        return PixelInfo(rPos, yToCanvas(v), buffColor)
    }

    private fun rSequenceToCoordinates(
        rSequence: RData,
        firstIteration: Int,
        pixSep: Int,
        yToCanvas: (Double) -> Int
    ): Collection<PixelInfo> {
        val rPos = rSequence.col * pixSep

        return rSequence.values
            .mapIndexed { i, v ->
                pixelInfo(i + firstIteration, v, rPos, yToCanvas)
            }
    }

    override fun dataToElementsToPlot(data0: List<RData>): IntArray {
        val h = super.h
        val w = super.w
        val buffer = IntArray((w + 1) * (h + 1)) { colorBytesCache.whiteBuffer }

        if (buffer.isNotEmpty()) {
            // Otimizações agressivas. Não precisa chamar os getters toda hora.
            val ym = yMin
            val deltaY = yMax - yMin
            val yToCanvas: (Double) -> Int = { y -> ((1 - (y - ym) / (deltaY)) * h).toInt() }
            val pixSep = pixelsSeparationProperty.value
            val firstIteration = firstIterationProperty.value

            data0
                .parallelStream()
                .flatMap { rSequenceToCoordinates(it, firstIteration, pixSep, yToCanvas).stream() }
                .filter { pi -> (0..h).contains(pi.yChart) }
                .forEach { pi ->
                    val pos = pi.xChart + pi.yChart * w

                    buffer[pos] = pi.colorBuffer
                }
        }

        return buffer
    }

}
