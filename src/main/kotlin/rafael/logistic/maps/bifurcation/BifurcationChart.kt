package rafael.logistic.maps.bifurcation

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.Circle
import rafael.logistic.view.getRainbowColor
import rafael.logistic.view.mapchart.MapChartBase
import tornadofx.*
import java.time.Duration
import java.time.LocalTime
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.streams.toList

private const val MAX_RADIUS = 1.0
private const val MIN_RADIUS = 0.4
private const val DELTA_RADIUS = MAX_RADIUS - MIN_RADIUS

class BifurcationChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartBase<RData>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    val x0Property = (0.0).toProperty()
    private val x0 by x0Property

    private fun rSequenceToElements(rSequence: RData): Stream<Circle> {
        val rChart = rSequence.r.realToChartX()
        return rSequence.values
                .mapIndexed { i, v -> Pair(v.realToChartY(), i.toDouble() / rSequence.values.size) }
                .parallelStream()
                .map { (xChart, pos) ->
                    Circle(rChart, xChart, (DELTA_RADIUS * pos + MIN_RADIUS)).apply {
                        stroke = getRainbowColor(pos)
                        fill = stroke
                    }
                }
    }

    override fun plotData() {
        if(x0 in myYAxis.lowerBound..myYAxis.lowerBound) {
            highlightP0(myXAxis.lowerBound, x0Property.value)
        }

        // TODO: Está sendo chamado 2 vezes ao redimensionar. Descobrir por quê.
//        Throwable().printStackTrace()

        val circles = data.parallelStream().flatMap { this.rSequenceToElements(it) }.toList()
        background.getChildList()?.addAll(circles)
    }

}