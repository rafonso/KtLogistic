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

    private val background: Node = super.lookup(".chart-plot-background")

    val observableData = emptyList<Double>().toProperty()

    val rProperty = (0.0).toProperty()

    init {
        (xAxis as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>
        (yAxis as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>
        background.style {
            backgroundColor += c("white")
        }
        observableData.onChange {
            layoutPlotChildren()
        }
    }

    private fun Double.toLogisticXPos() = xAxis.getDisplayPosition(this)

    private fun Double.toLogisticYPos() = yAxis.getDisplayPosition(this)

    private fun reloadData() {
        val zeroX = (0.0).toLogisticXPos()
        val zeroY = (0.0).toLogisticYPos()
        val halfX = (0.5).toLogisticXPos()
        val topY = (rProperty.value / 2).toLogisticYPos()
        val oneX = (1.0).toLogisticXPos()
        val oneY = (1.0).toLogisticYPos()

        background.add(Line(zeroX, zeroY, oneX, oneY)
                .also { it.stroke = c("blue") }
        )
        background.add(QuadCurve(zeroX, zeroY, halfX, topY, oneX, zeroY)
                .also {
                    it.stroke = c("green")
                    it.fill = c("transparent")
                }
        )
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
                    Line(coords[i - 1].first, coords[i - 1].second, coords[i].first, coords[i].second)
                            .apply {
                                stroke = getStroke(i.toDouble() / coords.size)
                                strokeWidth = (1.6 * i / coords.size + 0.4)
                            }
                }
                .forEach { l -> background.add(l) }
    }

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()
        reloadData()
        refreshData()
    }

}