package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.Line
import tornadofx.*

class IteractionChart(
        @NamedArg("xAxis") xAxis: Axis<Int>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Int, Double>>) : IteractionChartBase<Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Int>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().observable())

    override fun refreshData() {
        val coords = super.iteractionData.mapIndexed { i, d -> Pair(i.toIteractionsXPos(), d.toIteractionsYPos()) }
        (1 until coords.size)
                .map { i ->
                    Line(coords[i - 1].first, coords[i - 1].second, coords[i].first, coords[i].second).also { l ->
                        l.style {
                            stroke = getStroke(i.toDouble() / coords.size)
                        }
                    }
                }
                .forEach { l -> background.add(l) }
    }

}