package rafael.logistic.maps.julia

import javafx.scene.paint.Color
import rafael.logistic.view.getRainbowColor
import rafael.logistic.view.mapchart.CanvasChart
import rafael.logistic.view.mapchart.PixelInfo
import tornadofx.*
import java.util.stream.Collectors

class JuliaCanvas : CanvasChart<JuliaInfo>() {

    // @formatter:off

    val maxIterationsProperty   =   1.toProperty()
    private var maxIterations   by  maxIterationsProperty

    // @formatter:on

    override fun dataToElementsToPlot(): List<PixelInfo> {
        return data.parallelStream().map { ji ->
            Triple(
                    ji.col,
                    super.getHeight().toInt() - ji.row,
                    getRainbowColor(ji.iterationsToDiverge!!.toDouble() / maxIterations)
            )
        }.collect(Collectors.toList())
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