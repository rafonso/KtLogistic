package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.shape.Line
import javafx.scene.shape.QuadCurve
import javafx.util.StringConverter
import tornadofx.*

class LogisticChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : LineChart<Double, Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    private val logisticChartBackgound: Node = super.lookup(".chart-plot-background")

    val observableData = emptyList<Double>().toProperty()

    val rProperty = (0.0).toProperty()

    private val xyLine = Line((0.0).toLogisticXPos(), (0.0).toLogisticYPos(), (1.0).toLogisticXPos(), (1.0).toLogisticYPos()).also {
        it.stroke = c("blue")
    }

    init {
        (xAxis as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>
        (yAxis as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>
        logisticChartBackgound.style {
            backgroundColor += c("white")
        }

    }

    private fun Double.toLogisticXPos() = xAxis.getDisplayPosition(this)

    private fun Double.toLogisticYPos() = yAxis.getDisplayPosition(this)


    private fun reloadData() {
        logisticChartBackgound.add(
                Line((0.0).toLogisticXPos(), (0.0).toLogisticYPos(), (1.0).toLogisticXPos(), (1.0).toLogisticYPos()
                ).also {
                    it.stroke = c("blue")
                })
        logisticChartBackgound.add(
                QuadCurve((0.0).toLogisticXPos(), (0.0).toLogisticYPos(), (0.5).toLogisticXPos(), (rProperty.value / 2).toLogisticYPos(), (1.0).toLogisticXPos(), (0.0).toLogisticYPos()
                ).also {
                    it.stroke = c("green")
                    it.fill = c("transparent")
                })
    }

    private fun refreshData() {
        val data = observableData.value
        if (data.isEmpty()) {
            return
        }
        val coords = (listOf(Pair(data[0], 0.0)) + (1 until data.size)
                .flatMap { i -> listOf(Pair(data[i - 1], data[i]), Pair(data[i], data[i])) })
                .map { (x, y) -> Pair(x.toLogisticXPos(), y.toLogisticYPos()) }
        (1 until coords.size)
                .map { i ->
                    Line(coords[i - 1].first, coords[i - 1].second, coords[i].first, coords[i].second).also { l ->
                        l.style {
                            stroke = getStroke(i.toDouble() / coords.size)
                            strokeWidth = (1.6 * i / coords.size + 0.4).px
                        }
                    }
                }
                .forEach { l -> logisticChartBackgound.add(l) }
    }

    override fun layoutPlotChildren() {
        logisticChartBackgound.getChildList()?.clear()
        reloadData()
        refreshData()
    }

}