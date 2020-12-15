package rafael.logistic.set

import javafx.scene.image.PixelFormat
import javafx.scene.paint.Color
import rafael.logistic.core.fx.*
import rafael.logistic.core.fx.mapchart.CanvasChart
import tornadofx.*

class SetCanvas : CanvasChart<SetInfo, ByteArray>() {

    // @formatter:off

            val maxIterationsProperty   =   oneProperty()

    private val pixelFormat             =   PixelFormat.getByteRgbInstance()

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

    override fun dataToElementsToPlot(): Array<ByteArray> {
        val buffer = ByteArray(data.size * 3)

        data
            .map(SetInfo::iterationsToDiverge)
            .forEachIndexed { i, iterations -> fillBuffer(buffer, i, cacheIteration[iterations]) }

        return arrayOf(buffer)
    }

    private fun fillBuffer(buffer: ByteArray, i: Int, bc: ByteArray) {
        buffer[i * 3 + 0] = bc[0]
        buffer[i * 3 + 1] = bc[1]
        buffer[i * 3 + 2] = bc[2]
    }

    override fun plotData(elements: Array<ByteArray>) {
        pixelWriter.setPixels(0, 0, super.w, super.h, pixelFormat, elements[0], 0, super.w * 3)
    }

    override fun finalizePlotting() {

        fun plotAxis(x1: Double, y1: Double, x2: Double, y2: Double) {
            gc.stroke = Color.GREY
            gc.lineWidth = 1.0
            gc.strokeLine(x1.realToCanvasX(), y1.realToCanvasY(), x2.realToCanvasX(), y2.realToCanvasY())
        }

        if (0.0 in super.xMin..super.xMax) {
            plotAxis(super.xMin, 0.0, super.xMax, 0.0)
        }
        if (0.0 in super.yMin..super.yMax) {
            plotAxis(0.0, super.yMax, 0.0, super.yMin)
        }
    }

}
