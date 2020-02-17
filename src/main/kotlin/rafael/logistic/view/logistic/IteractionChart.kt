package rafael.logistic.view.logistic

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.shape.Line
import rafael.logistic.view.CONVERTER_0
import rafael.logistic.view.CONVERTER_2
import rafael.logistic.view.getStroke
import tornadofx.*

class IteractionChart(
        @NamedArg("xAxis") xAxis: Axis<Int>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Int, Double>>) : LineChart<Int, Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Int>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().observable())

    private val background: Node = super.lookup(".chart-plot-background")

    val observableData = emptyList<Double>().toProperty()

    val iteractionsProperty = 0.toProperty()

    init {
        (xAxis as NumberAxis).tickLabelFormatter = CONVERTER_0
        (yAxis as NumberAxis).tickLabelFormatter = CONVERTER_2
        background.style {
            backgroundColor += c("white")
        }
        iteractionsProperty.onChange {
            xAxis.upperBound = it.toDouble()
        }
        xAxis.tickUnitProperty().bind(xAxis.upperBoundProperty().divide(10))
        observableData.onChange {
            layoutPlotChildren()
        }
    }

    private fun Int.toIteractionsXPos() = xAxis.getDisplayPosition(this)

    private fun Double.toIteractionsYPos() = yAxis.getDisplayPosition(this)

    private fun refreshData() {
        val data = observableData.value
        if (data.isEmpty()) {
            return
        }

        val coords = data.mapIndexed { i, d -> Pair(i.toIteractionsXPos(), d.toIteractionsYPos()) }
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

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()
        refreshData()
    }

}