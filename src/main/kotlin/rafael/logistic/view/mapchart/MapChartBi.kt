package rafael.logistic.view.mapchart

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.Circle
import javafx.geometry.Point2D
import rafael.logistic.view.getRainbowColor
import tornadofx.*
import java.util.stream.Collectors

private const val MAX_RADIUS = 1.0
private const val MIN_RADIUS = 0.4
private const val DELTA_RADIUS = MAX_RADIUS - MIN_RADIUS

class MapChartBi(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartBase<Point2D>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

//    val x0Property = square.xProperty
//    var x0 by x0Property
//
//    val y0Property = square.yProperty
//    var y0 by x0Property


    private fun refreshData() {
        val elements = data
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

        background.getChildList()?.addAll(elements)
    }

    override fun plotData() {
        if (data.isNotEmpty()) {
            // Destaca o x0 e y0
            highlightP0(data.first().x, data.first().y)
            refreshData()
        }
    }

//    override fun p0Moved(square: Point0, pos: Point2D) {
//        if(pos.x >= 0 && pos.x <= background.layoutBounds.maxX) {
//            square.x = pos.x
//        }
//        if(pos.y >= 0 && pos.y <= background.layoutBounds.maxY) {
//            square.y = pos.y
//        }
//    }

}
