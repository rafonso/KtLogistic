package rafael.logistic.set

import rafael.logistic.core.fx.ColorIntCache
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.rainbowColors
import tornadofx.*

class SetCanvas : CanvasChart<SetInfo>() {

    // @formatter:off

            val maxIterationsProperty   =   oneProperty()

    private val cacheByMaxIteratrions   =   mutableMapOf<Int, IntArray>()

    private var cacheIteration          :   IntArray = intArrayOf()

    private val colorBytesCache         =   ColorIntCache(rainbowColors)

    // @formatter:on

    init {
        super.initialize()

        maxIterationsProperty.onChange { maxIt ->
            cacheIteration = cacheByMaxIteratrions.getOrPut(maxIt) {
                IntArray(maxIt + 1) {
                    if (it == 0) ColorIntCache.black
                    else colorBytesCache.get(it, maxIt)
                }
            }
        }
    }

    override fun dataToElementsToPlot(data0: List<SetInfo>): IntArray =
        data0
            .map(SetInfo::iterationsToDiverge)
            .map(cacheIteration::get)
            .toIntArray()

}
