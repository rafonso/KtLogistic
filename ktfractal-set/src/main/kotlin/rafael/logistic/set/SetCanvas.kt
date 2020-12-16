package rafael.logistic.set

import rafael.logistic.core.fx.*
import rafael.logistic.core.fx.mapchart.CanvasChart
import tornadofx.*

class SetCanvas : CanvasChart<SetInfo>() {

    // @formatter:off

            val maxIterationsProperty   =   oneProperty()

    private val cacheByMaxIteratrions   =   mutableMapOf<Int, Array<ByteArray>>()

    private var cacheIteration          :   Array<ByteArray> = emptyArray()

    // @formatter:on

    init {
        super.initialize()

        maxIterationsProperty.onChange { maxIt ->
            cacheIteration = cacheByMaxIteratrions.getOrPut(maxIt) {
                Array(maxIt + 1) {
                    if (it == 0) blackBuffer
                    else getRainbowColor(it.toDouble() / maxIt).toBytes()
                }
            }
        }
    }

    override fun dataToElementsToPlot(): ByteArray =
        data
            .map(SetInfo::iterationsToDiverge)
            .foldRightIndexed(ByteArray(data.size * 3)) { i, iterations, buffer ->
                buffer.addBuffer(i, cacheIteration[iterations])
            }

}
