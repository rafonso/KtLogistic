package rafael.logistic.view.iterationchart

import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import rafael.logistic.view.CONVERTER_0
import rafael.logistic.view.CONVERTER_2
import tornadofx.*

abstract class IterationChartBase<T>(
        xAxis: Axis<Int>,
        yAxis: Axis<Double>,
        data: ObservableList<Series<Int, Double>>) : LineChart<Int, Double>(xAxis, yAxis, data) {

    protected val background: Node = super.lookup(".chart-plot-background")

    private val iterationDataProperty = emptyList<T>().toProperty()
    protected var iterationData: List<T> by iterationDataProperty

    private val iterationsProperty = 0.toProperty()

    protected val valueYAxis = (yAxis as NumberAxis)

    init {
        (xAxis as NumberAxis).tickLabelFormatter = CONVERTER_0
        valueYAxis.tickLabelFormatter = CONVERTER_2
        background.style {
            backgroundColor += c("white")
        }
        iterationsProperty.onChange {
            xAxis.upperBound = it.toDouble()
        }
        xAxis.tickUnitProperty().bind(xAxis.upperBoundProperty().divide(10))
        iterationDataProperty.onChange {
            layoutPlotChildren()
        }
    }

    protected fun Int.toIterationsXPos() = xAxis.getDisplayPosition(this)

    protected fun Double.toIterationsYPos() = valueYAxis.getDisplayPosition(this)

    protected abstract fun refreshData()

    fun bind(valueProperty: ReadOnlyObjectProperty<Int>, observableData: ObjectProperty<List<T>>) {
        this.iterationsProperty.bind(valueProperty)
        this.iterationDataProperty.bind(observableData)
    }

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()

        if (iterationData.isNotEmpty()) {
            refreshData()
        }
    }

}
