package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import tornadofx.*

abstract class MapChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : LineChart<Double, Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    protected val background: Node = super.lookup(".chart-plot-background")

    val dataProperty = emptyList<Double>().toProperty()
    protected var data: List<Double> by dataProperty

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

    protected fun Double.toLogisticXPos() = xAxis.getDisplayPosition(this)

    protected fun Double.toLogisticYPos() = yAxis.getDisplayPosition(this)

    protected abstract fun refreshData()

    protected abstract fun reloadData()

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()
        reloadData()
        if(data.isNotEmpty()) {
            refreshData()
        }
    }

}