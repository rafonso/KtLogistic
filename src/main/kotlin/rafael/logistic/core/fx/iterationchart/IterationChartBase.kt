package rafael.logistic.core.fx.iterationchart

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.PathElement
import rafael.logistic.core.fx.CONVERTER_0
import rafael.logistic.core.fx.CONVERTER_2
import rafael.logistic.core.fx.rainbow
import tornadofx.*

abstract class IterationChartBase<T>(
        xAxis: Axis<Int>,
        yAxis: Axis<Double>,
        data: ObservableList<Series<Int, Double>>) : LineChart<Int, Double>(xAxis, yAxis, data) {

    private val background: Node = super.lookup(".chart-plot-background")

    private val iterationDataProperty = emptyList<T>().toProperty()

    private val iterationsProperty = 0.toProperty()

    private val valueXAxis = (xAxis as NumberAxis)

    protected val valueYAxis = (yAxis as NumberAxis)

    private val gradient = LinearGradient(0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE,
            rainbow.mapIndexed { i, color -> Stop(i / 6.0, color) })

    init {
        valueXAxis.tickLabelFormatter = CONVERTER_0
        valueYAxis.tickLabelFormatter = CONVERTER_2
        iterationsProperty.onChange {
            valueXAxis.upperBound = it.toDouble()
        }
        valueXAxis.tickUnitProperty().bind(valueXAxis.upperBoundProperty().divide(10))
        iterationDataProperty.onChange {
            layoutPlotChildren()
        }
    }

    protected fun Int.toIterationsXPos() = xAxis.getDisplayPosition(this)

    protected fun Double.toIterationsYPos() = valueYAxis.getDisplayPosition(this)

    protected abstract fun loadPath(iterationData: List<T>): Array<PathElement>

    fun bind(valueProperty: ReadOnlyObjectProperty<Int>, observableData: ReadOnlyObjectProperty<List<T>>) {
        this.iterationsProperty.bind(valueProperty)
        this.iterationDataProperty.bind(observableData)
    }

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()

        if (iterationDataProperty.value.isNotEmpty()) {
            val elements = loadPath(iterationDataProperty.value)
            background.add(path(*elements) {
                this.stroke = gradient
            })
        }
    }

}
