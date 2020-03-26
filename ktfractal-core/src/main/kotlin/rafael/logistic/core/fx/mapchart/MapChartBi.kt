package rafael.logistic.core.fx.mapchart

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import rafael.logistic.core.generation.BiDouble
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.shape.Circle
import rafael.logistic.core.fx.getRainbowColor
import tornadofx.*
import java.util.stream.Collectors

private const val MAX_RADIUS = 1.0
private const val MIN_RADIUS = 0.4
private const val DELTA_RADIUS = MAX_RADIUS - MIN_RADIUS

class MapChartBi(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartBase<BiDouble>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    override fun initialize() {
        super.prefWidthProperty().bindBidirectional(super.prefHeightProperty())
    }

    override fun prepareBackground() {
        background.getChildList()?.clear()
        // Destaca o x0 e y0
        highlightP0(data.first().x, data.first().y)
    }

    override fun dataToElementsToPlot(): List<Node> = data
            .filter { p ->
                (p.x >= myXAxis.lowerBound) && (p.x <= myXAxis.upperBound) &&
                        (p.y >= myYAxis.lowerBound) && (p.y <= myYAxis.upperBound)
            }
            .mapIndexed { i, p -> Triple(p.x.realToChartX(), p.y.realToChartY(), i.toDouble() / data.size) }
            .parallelStream()
            .map { t ->
                Circle(t.first, t.second, (DELTA_RADIUS * t.third + MIN_RADIUS)).apply {
                    stroke = getRainbowColor(t.third)
                    fill = stroke
                }
            }
            .collect(Collectors.toList())

    override fun plotData(elements: List<Node>) {
        background.getChildList()?.addAll(elements)
    }

}
