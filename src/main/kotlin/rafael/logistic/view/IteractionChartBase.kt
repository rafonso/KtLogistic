package rafael.logistic.view

import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import tornadofx.*

abstract class IteractionChartBase<T>(
        xAxis: Axis<Int>,
        yAxis: Axis<Double>,
        data: ObservableList<Series<Int, Double>>) : LineChart<Int, Double>(xAxis, yAxis, data) {

    constructor(xAxis: Axis<Int>, yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().observable())

    protected val background: Node = super.lookup(".chart-plot-background")

    val iteractionDataProperty = emptyList<T>().toProperty()
    protected var iteractionData: List<T> by iteractionDataProperty

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
        iteractionDataProperty.onChange {
            layoutPlotChildren()
        }
    }

    protected fun Int.toIteractionsXPos() = xAxis.getDisplayPosition(this)

    protected fun Double.toIteractionsYPos() = yAxis.getDisplayPosition(this)

    protected abstract fun refreshData()

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()

        if (iteractionData.isNotEmpty()) {
            refreshData()
        }
    }

}