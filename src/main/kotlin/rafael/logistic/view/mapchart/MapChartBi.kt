package rafael.logistic.view.mapchart

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import rafael.logistic.generator.BiPoint
import tornadofx.*

const val P0_SIDE = 10.0

class MapChartBi(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartBase<BiPoint>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())


    private fun refreshData() {
        data
                .filter { p ->
                    (p.x >= myXAxis.lowerBound) && (p.x <= myXAxis.upperBound) &&
                            (p.y >= myYAxis.lowerBound) && (p.y <= myYAxis.upperBound)
                }
                .map { p -> Pair(p.x.toLogisticXPos(), p.y.toLogisticYPos()) }
                .mapIndexed { index, pair ->
                    circle(pair.first, pair.second, (1.6 * index / data.size + 0.4)).apply {
                        stroke = rafael.logistic.view.getStroke(index.toDouble() / data.size)
                        fill = stroke
                    }
                }
                .forEach { l -> background.add(l) }
    }

    override fun plotData() {
        if (data.isNotEmpty()) {
            // Destaca o x0 e y0
            highlightP0(data.first().x, data.first().y)
            refreshData()
        }
    }

}
