package rafael.logistic.maps.julia

import rafael.logistic.view.getRainbowColor
import rafael.logistic.view.mapchart.CanvasChart
import rafael.logistic.view.mapchart.PixelInfo
import tornadofx.*

class JuliaCanvas : CanvasChart<JuliaInfo>() {

    // @formatter:off

    val maxIterationsProperty   =   1.toProperty()
    private var maxIterations   by  maxIterationsProperty

    // @formatter:on


    override fun dataToElementsToPlot(): List<PixelInfo> {
        return data.map { ji ->
            Triple(
                    ji.x.realToCanvasX().toInt(),
                    ji.y.realToCanvasY().toInt(),
                    getRainbowColor(ji.iterationsToDiverge!!.toDouble() / maxIterations)
            )
        }
    }
}