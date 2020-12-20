package rafael.logistic.set

import rafael.logistic.core.fx.ColorBytesCache
import rafael.logistic.core.fx.addBuffer
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.rainbowColors
import tornadofx.*

class SetCanvas : CanvasChart<SetInfo>() {

    // @formatter:off

            val maxIterationsProperty   =   oneProperty()

    private val cacheByMaxIteratrions   =   mutableMapOf<Int, Array<ByteArray>>()

    private var cacheIteration          :   Array<ByteArray> = emptyArray()

    private val colorBytesCache         =   ColorBytesCache(rainbowColors)

    // @formatter:on

    init {
        super.initialize()

        maxIterationsProperty.onChange { maxIt ->
            cacheIteration = cacheByMaxIteratrions.getOrPut(maxIt) {
                Array(maxIt + 1) {
                    if (it == 0) ColorBytesCache.blackBuffer
                    else colorBytesCache.getBytes(it, maxIt)
                }
            }
        }
    }

    override fun dataToElementsToPlot(data0: List<SetInfo>): ByteArray =
        data0
            .map(SetInfo::iterationsToDiverge)
            .foldRightIndexed(ByteArray(data0.size * 3)) { i, iterations, buffer ->
                buffer.addBuffer(i, cacheIteration[iterations])
            }

}
