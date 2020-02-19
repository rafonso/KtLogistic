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

        plotLines(coords, background) { l, i ->
            l.stroke = getStroke(i.toDouble() / coords.size)
            l.strokeWidth = (1.6 * i / coords.size + 0.4)
        }
    }

    protected fun Double.toLogisticXPos() = xAxis.getDisplayPosition(this)

    protected fun Double.toLogisticYPos() = yAxis.getDisplayPosition(this)

    protected abstract fun recalculateBounds()

    abstract fun getBounds(): List<Pair<Double, Double>>

    private fun refreshXY() {
        plotLines(getBounds(), background) { l, _ -> l.stroke = c("blue") }
    }

    protected abstract fun refreshAsymptote()

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()

        recalculateBounds()
        refreshXY()
        refreshAsymptote()
        if (data.isNotEmpty()) {
            refreshData()
        }
    }

}