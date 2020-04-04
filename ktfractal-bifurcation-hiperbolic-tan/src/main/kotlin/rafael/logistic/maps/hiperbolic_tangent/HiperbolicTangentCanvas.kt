package rafael.logistic.maps.hiperbolic_tangent

import rafael.logistic.core.fx.getRainbowColor
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.mapchart.PixelInfo
import rafael.logistic.maps.hiperbolic_tangent.data.GData
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class HiperbolicTangentCanvas : CanvasChart<GData>() {

    // @formatter:off

    private val pixelsSeparationProperty    =   1.toProperty()
            var pixelsSeparation            by  pixelsSeparationProperty

    // @formatter:on

    private fun rSequenceToCoordinates(rSequence: GData, pixSep: Int, yToCanvas: (Double) -> Int): List<PixelInfo> {
        // Depepdendo do convergenceType, o tamanho rSequence.values pode variar
        val size = rSequence.values.size

        return rSequence.values
            .mapIndexed { i, v ->
                Triple(rSequence.col * pixSep, yToCanvas(v), getRainbowColor(i.toDouble() / size))
            }
    }

    override fun dataToElementsToPlot(): List<PixelInfo> {
        // Otimizações agressivas. Não precisa chamar os getters toda hora.
        val ym = yMin
        val deltaY = yMax - yMin
        val h = super.getHeight()
        val yToCanvas: (Double) -> Int = { y -> ((1 - (y - ym) / (deltaY)) * h).toInt() }
        val pixSep = pixelsSeparationProperty.value

        return data.flatMap { this.rSequenceToCoordinates(it, pixSep, yToCanvas) }
    }

}
