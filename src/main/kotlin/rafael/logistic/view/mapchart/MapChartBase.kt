package rafael.logistic.view.mapchart

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import rafael.logistic.view.CONVERTER_2
import tornadofx.*

abstract class MapChartBase<T>(
        xAxis: Axis<Double>,
        yAxis: Axis<Double>,
        data: ObservableList<Series<Double, Double>>) : LineChart<Double, Double>(xAxis, yAxis, data) {

    constructor(xAxis: Axis<Double>, yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    protected val background: Node = super.lookup(".chart-plot-background")

    private val dataProperty = emptyList<T>().toProperty()
    protected var data: List<T> by dataProperty

    protected val myXAxis = (xAxis as NumberAxis)

    protected val myYAxis = (yAxis as NumberAxis)

    init {
        myXAxis.tickLabelFormatter = CONVERTER_2
        myYAxis.tickLabelFormatter = CONVERTER_2
        background.style {
            backgroundColor += c("white")
        }
        dataProperty.onChange {
            layoutPlotChildren()
        }
    }

    protected fun Double.toLogisticXPos() = myXAxis.getDisplayPosition(this)

    protected fun Double.toLogisticYPos() = myYAxis.getDisplayPosition(this)

    protected abstract fun plotData()

    fun bind(dataProperty: ReadOnlyObjectProperty<List<T>>, handler: (MapChartBase<T>) -> Unit = {}) {
        this.dataProperty.bind(dataProperty)
        handler(this)
    }

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()
        plotData()
    }

}
