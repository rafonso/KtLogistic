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
            .forEachIndexed { i, iterations -> buffer.addBuffer(i, cacheIteration[iterations]) }

        return arrayOf(buffer)
    }

    override fun plotData(elements: Array<ByteArray>) {
        pixelWriter.setPixels(0, 0, super.w, super.h, pixelFormat, elements[0], 0, super.w * 3)
    }

    override fun finalizePlotting() {

        fun plotAxis(x1: Double, y1: Double, x2: Double, y2: Double) {
            gc.stroke = Color.GREY
            gc.lineWidth = 1.0
            gc.strokeLine(x1, y1, x2, y2)
        }

        if (0.0 in super.xMin..super.xMax) {
            val x0Canvas = 0.0.realToCanvasX()
            plotAxis( x0Canvas, 0.0, x0Canvas, super.getHeight())
        }
        if (0.0 in super.yMin..super.yMax) {
            val y0Canvas = 0.0.realToCanvasY()
            plotAxis(0.0, y0Canvas, super.getWidth(), y0Canvas)
        }
    }

}
