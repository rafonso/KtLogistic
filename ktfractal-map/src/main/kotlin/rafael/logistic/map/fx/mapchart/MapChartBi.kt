package rafael.logistic.map.fx.mapchart

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.shape.Circle
import rafael.logistic.core.fx.getRainbowColor
import rafael.logistic.core.generation.BiDouble
import tornadofx.*

private const val MAX_RADIUS = 1.0
private const val MIN_RADIUS = 0.4
private const val DELTA_RADIUS = MAX_RADIUS - MIN_RADIUS

class MapChartBi(
    @NamedArg("xAxis") xAxis: Axis<Double>,
    @NamedArg("yAxis") yAxis: Axis<Double>,
    @NamedArg("data") data: ObservableList<Series<Double, Double>>
) : MapChartBase<BiDouble>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().asObservable<Series<Double, Double>>())

    private fun coordsToCircle(centerX: Double, centerY: Double, pos: Double) =
        Circle(centerX, centerY, (DELTA_RADIUS * pos + MIN_RADIUS)).apply {
            stroke = getRainbowColor(pos)
            fill = stroke
        }

    private fun biDoubleToCoords(i: Int, p: BiDouble) =
        Triple(p.x.realToChartX(), p.y.realToChartY(), i.toDouble() / data.size)

    private fun insideBounds(p: BiDouble) =
        (p.x >= myXAxis.lowerBound) && (p.x <= myXAxis.upperBound) &&
                (p.y >= myYAxis.lowerBound) && (p.y <= myYAxis.upperBound)

    override fun initialize() {
        super.prefWidthProperty().bindBidirectional(super.prefHeightProperty())
    }

    override fun prepareBackground(data0: List<BiDouble>) {
        background.getChildList()?.clear()
        // Destaca o x0 e y0
        highlightP0(data0.first().x, data0.first().y)
    }

    override fun dataToElementsToPlot(data0: List<BiDouble>): Array<Node> = data0
        .filter(this::insideBounds)
        .mapIndexed(this::biDoubleToCoords)
        .parallelStream()
        .map { (centerX, centerY, pos) ->
            coordsToCircle(centerX, centerY, pos)
        }
        .toArray(::arrayOfNulls)

    override fun plotData(element: Array<Node>) {
        background.getChildList()?.addAll(element)
    }

}
