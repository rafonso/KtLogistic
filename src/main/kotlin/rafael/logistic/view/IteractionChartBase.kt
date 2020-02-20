package rafael.logistic.view

import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
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

    private val iteractionDataProperty = emptyList<T>().toProperty()
    protected var iteractionData: List<T> by iteractionDataProperty

    private val iteractionsProperty = 0.toProperty()

    protected val valueYAxis = (yAxis as NumberAxis)

    init {
        (xAxis as NumberAxis).tickLabelFormatter = CONVERTER_0
        valueYAxis.tickLabelFormatter = CONVERTER_2
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

    protected fun Double.toIteractionsYPos() = valueYAxis.getDisplayPosition(this)

    protected abstract fun refreshData()

    fun bind(valueProperty: ReadOnlyObjectProperty<Int>, observablData: ObjectProperty<List<T>>) {
        this.iteractionsProperty.bind(valueProperty)
        this.iteractionDataProperty.bind(observablData)
    }

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()

        if (iteractionData.isNotEmpty()) {
            refreshData()
        }
    }

}