package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.shape.Line
import tornadofx.*

abstract class MapChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : LineChart<Double, Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    protected val background: Node = super.lookup(".chart-plot-background")

    val dataProperty = emptyList<Double>().toProperty()
    private var data: List<Double> by dataProperty

    init {
        (xAxis as NumberAxis).tickLabelFormatter = CONVERTER_2
        (yAxis as NumberAxis).tickLabelFormatter = CONVERTER_2
        background.style {
            backgroundColor += c("white")
        }
        dataProperty.onChange {
            layoutPlotChildren()
        }
    }

    private fun refreshData() {
        val coords = (listOf(Pair(data[0], 0.0)) + (1 until data.size)
                .flatMap { i -> listOf(Pair(data[i - 1], data[i]), Pair(data[i], data[i])) })
                .map { (x, y) -> Pair(x.toLogisticXPos(), y.toLogisticYPos()) }
        (1 until coords.size)
                .map { i ->
                    Line(coords[i - 1].first, coords[i - 1].second, coords[i].first, coords[i].second)
                            .apply {
                                stroke = getStroke(i.toDouble() / coords.size)
                                strokeWidth = (1.6 * i / coords.size + 0.4)
                            }
                }
                .forEach { l -> background.add(l) }
    }

    protected fun Double.toLogisticXPos() = xAxis.getDisplayPosition(this)

    protected fun Double.toLogisticYPos() = yAxis.getDisplayPosition(this)

    protected abstract fun recalculateBounds()

    protected abstract fun refreshXY()

    protected abstract fun refreshAsymptote()

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()

        recalculateBounds()
        refreshXY()
        refreshAsymptote()
        if(data.isNotEmpty()) {
            refreshData()
        }
    }

}