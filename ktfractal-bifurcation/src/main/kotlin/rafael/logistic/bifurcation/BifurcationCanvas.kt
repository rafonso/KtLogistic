package rafael.logistic.bifurcation

import javafx.scene.paint.Color
import rafael.logistic.core.fx.getRainbowColor
import rafael.logistic.core.fx.mapchart.CanvasChart
import tornadofx.*

class BifurcationCanvas : CanvasChart<RData, PixelInfo>() {

    // @formatter:off

    val pixelsSeparationProperty    =   1.toProperty()

    // @formatter:on

    private fun rSequenceToCoordinates(
        rSequence: RData,
        pixSep: Int,
        yToCanvas: (Double) -> Int,
        colorCache: MutableMap<Int, Color>
    ): Collection<PixelInfo> {
        val size = rSequence.values.size
        val rPos = rSequence.col * pixSep

        val result = mutableSetOf<PixelInfo>()

        rSequence.values.reversed()
            .mapIndexed { i, v ->
                PixelInfo(
                    rPos,
                    yToCanvas(v),
                    colorCache.getOrPut(size - i) { getRainbowColor((size - i).toDouble() / size) })
            }
            .forEach(result::add)

        return result
    }

    override fun plotData(elements: Array<PixelInfo>) {
        elements.forEach { pi -> pixelWriter.setColor(pi.xChart, pi.yChart, pi.color) }
    }

    override fun dataToElementsToPlot(): Array<PixelInfo> {
        // Otimizações agressivas. Não precisa chamar os getters toda hora.
        val ym = yMin
        val deltaY = yMax - yMin
        val h = super.getHeight()
        val yToCanvas: (Double) -> Int = { y -> ((1 - (y - ym) / (deltaY)) * h).toInt() }
        val pixSep = pixelsSeparationProperty.value
        val colorCache = mutableMapOf<Int, Color>()

        return data.parallelStream()
            .flatMap { this.rSequenceToCoordinates(it, pixSep, yToCanvas, colorCache).stream() }
            .toArray(::arrayOfNulls)
    }

}
