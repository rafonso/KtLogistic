package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.shape.Line
import rafael.logistic.generatorbi.BiPoint
import tornadofx.*

class MapChartBi(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : LineChart<Double, Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    private val background: Node = super.lookup(".chart-plot-background")

    val dataProperty = emptyList<BiPoint>().toProperty()
    private var data: List<BiPoint> by dataProperty

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
        data
                .map { p -> Pair(p.x.toLogisticXPos(), p.y.toLogisticYPos()) }
                .mapIndexed { index, pair ->
                    circle(pair.first, pair.second, (1.6 * index / data.size + 0.4)).apply {
                        stroke = getStroke(index.toDouble() / data.size)
                        fill = stroke
                    }
                }
                .forEach { l -> background.add(l) }
    }

    protected fun Double.toLogisticXPos() = xAxis.getDisplayPosition(this)

    protected fun Double.toLogisticYPos() = yAxis.getDisplayPosition(this)

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()

        if (data.isNotEmpty()) {
            refreshData()
        }
    }

}