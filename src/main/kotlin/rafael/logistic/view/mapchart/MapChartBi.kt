package rafael.logistic.view.mapchart

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.Circle
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.getStroke
import tornadofx.*
import java.util.stream.Collectors

class MapChartBi(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartBase<BiPoint>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())


    private fun refreshData() {
        val elements = data
                .filter { p ->
                    (p.x >= myXAxis.lowerBound) && (p.x <= myXAxis.upperBound) &&
                            (p.y >= myYAxis.lowerBound) && (p.y <= myYAxis.upperBound)
                }
                .mapIndexed { i, p -> Triple(p.x.toLogisticXPos(), p.y.toLogisticYPos(), i.toDouble() / data.size) }
                .parallelStream()
                .map { t ->
                    Circle(t.first, t.second, (1.6 * t.third + 0.4)).apply {
                        stroke = getStroke(t.third)
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

}
