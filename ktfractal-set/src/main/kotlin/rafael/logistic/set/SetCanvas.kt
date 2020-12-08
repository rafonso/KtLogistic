package rafael.logistic.set

import javafx.scene.paint.Color
import rafael.logistic.core.fx.getRainbowColor
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.mapchart.PixelInfo
import rafael.logistic.core.fx.oneProperty
import tornadofx.*

class SetCanvas : CanvasChart<SetInfo>() {

    // @formatter:off

    val maxIterationsProperty   =   oneProperty()
    private var maxIterations   by  maxIterationsProperty

    // @formatter:on

    override fun dataToElementsToPlot(): Array<PixelInfo> {
        val height = super.getHeight().toInt()

        return data.parallelStream()
            .map { ji ->
                val color = getRainbowColor(ji.iterationsToDiverge!!.toDouble() / maxIterations)
                Triple(ji.col, height - ji.row, color)
            }
            .toArray(::arrayOfNulls)
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
