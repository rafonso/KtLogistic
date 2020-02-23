package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import tornadofx.*

class IterationChartDouble(
        @NamedArg("xAxis") xAxis: Axis<Int>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Int, Double>>) : IterationChartBase<Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Int>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().observable())

    override fun refreshData() {
        val coords = super.iterationData.mapIndexed { i, d -> Pair(i.toIterationsXPos(), d.toIterationsYPos()) }
        plotLines(coords, background) { l, i ->
            l.style {
                stroke = getStroke(i.toDouble() / coords.size)
            }
        }
    }

}
