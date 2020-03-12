package rafael.logistic.maps.bifurcation

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.geometry.Point2D
import javafx.scene.chart.Axis
import javafx.scene.shape.Circle
import rafael.logistic.view.getRainbowColor
import rafael.logistic.view.mapchart.MapChartBase
import tornadofx.*
import java.util.stream.Collectors

private const val MAX_RADIUS = 1.0
private const val MIN_RADIUS = 0.4
private const val DELTA_RADIUS = MAX_RADIUS - MIN_RADIUS

class BifurcationChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartBase<List<Point2D>>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    private fun rSequenceToElements(rSequence: List<Point2D>) =
            rSequence
                    .mapIndexed { i, p -> Triple(p.x.realToChartX(), p.y.realToChartY(), i.toDouble() / rSequence.size) }
                    .parallelStream()
                    .map { t ->
                        Circle(t.first, t.second, (DELTA_RADIUS * t.third + MIN_RADIUS)).apply {
                            stroke = getRainbowColor(t.third)
                            fill = stroke
                        }
                    }
                    .collect(Collectors.toList())


    override fun plotData() {
        highlightP0(0.0, data[0][0].y)
        // TODO: Está sendo chamado 2 vezes ao redimensionar. Descobrir por quê.
//        Throwable().printStackTrace()
        data.map (this::rSequenceToElements).forEach { elements -> background.getChildList()?.addAll(elements) }
    }

}