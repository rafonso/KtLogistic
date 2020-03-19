package rafael.logistic.maps.bifurcation.canvas

import rafael.logistic.maps.bifurcation.RData
import rafael.logistic.view.getRainbowColor
import rafael.logistic.view.mapchart.CanvasChart
import rafael.logistic.view.mapchart.PixelInfo
import java.util.stream.Stream
import kotlin.streams.toList

class BifurcationCanvas : CanvasChart<RData>() {

    // @formatter:off


    // @formatter:on

    private fun rSequenceToCoordinates(rSequence: RData): Stream<PixelInfo> {
        val size = rSequence.values.size

        return rSequence.values
                .mapIndexed { i, v -> Pair(v.realToCanvasY().toInt(), i.toDouble() / size) }
                .parallelStream()
                .map { Triple(rSequence.col, it.first, getRainbowColor(it.second)) }
    }

    override fun dataToElementsToPlot() = data
            .parallelStream()
            .flatMap(this::rSequenceToCoordinates).toList()

}
