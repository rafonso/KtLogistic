package rafael.logistic.maps.bifurcation.canvas

import rafael.logistic.core.fx.getRainbowColor
import rafael.logistic.core.fx.mapchart.CanvasChart
import rafael.logistic.core.fx.mapchart.PixelInfo
import rafael.logistic.maps.bifurcation.RData
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty
import java.util.stream.Stream
import kotlin.streams.toList

class BifurcationCanvas : CanvasChart<RData>() {

    // @formatter:off

    private val pixelsSeparationProperty    =   1.toProperty()
            var pixelsSeparation            by  pixelsSeparationProperty

    // @formatter:on

    private fun rSequenceToCoordinates(rSequence: RData): Stream<PixelInfo> {
        val size = rSequence.values.size

        return rSequence.values
            .mapIndexed { i, v -> Pair(v.realToCanvasY().toInt(), i.toDouble() / size) }
            .parallelStream()
            .map { Triple(rSequence.col * pixelsSeparation, it.first, getRainbowColor(it.second)) }
    }

    override fun dataToElementsToPlot() = data
        .parallelStream()
        .flatMap(this::rSequenceToCoordinates)
        .toList()

}
