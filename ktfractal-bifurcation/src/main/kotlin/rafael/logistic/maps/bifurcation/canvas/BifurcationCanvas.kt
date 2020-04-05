package rafael.logistic.maps.bifurcation.canvas

import javafx.scene.paint.Color
import rafael.logistic.core.fx.getRainbowColor
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.mapchart.PixelInfo
import rafael.logistic.maps.bifurcation.RData
import tornadofx.toProperty

class BifurcationCanvas : CanvasChart<RData>() {

    // @formatter:off

    val pixelsSeparationProperty    =   1.toProperty()

    // @formatter:on

    private fun rSequenceToCoordinates(
        rSequence: RData,
        pixSep: Int,
        yToCanvas: (Double) -> Int,
        colorCache: MutableMap<Int, Color>
    ): List<PixelInfo> {
        val size = rSequence.values.size
        val rPos = rSequence.col * pixSep

        return rSequence.values
            .mapIndexed { i, v ->
                Triple(
                    rPos,
                    yToCanvas(v),
                    colorCache.getOrPut(i) { getRainbowColor(i.toDouble() / size) })
            }
    }

    override fun dataToElementsToPlot(): List<PixelInfo> {
        // Otimizações agressivas. Não precisa chamar os getters toda hora.
        val ym = yMin
        val deltaY = yMax - yMin
        val h = super.getHeight()
        val yToCanvas: (Double) -> Int = { y -> ((1 - (y - ym) / (deltaY)) * h).toInt() }
        val pixSep = pixelsSeparationProperty.value
        val colorCache = mutableMapOf<Int, Color>()

        return data.flatMap { this.rSequenceToCoordinates(it, pixSep, yToCanvas, colorCache) }
    }

}
