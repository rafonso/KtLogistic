package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import tornadofx.*

abstract class MapChartDouble(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartBase<Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    private fun refreshData() {
        val coords = (listOf(Pair(data[0], 0.0)) + (1 until data.size)
                .flatMap { i -> listOf(Pair(data[i - 1], data[i]), Pair(data[i], data[i])) })
                .map { (x, y) -> Pair(x.toLogisticXPos(), y.toLogisticYPos()) }

        plotLines(coords, background) { l, i ->
            l.stroke = getStroke(i.toDouble() / coords.size)
            l.strokeWidth = (1.6 * i / coords.size + 0.4)
        }
    }

    private fun refreshXY() {
        plotLines(getBounds(), background) { l, _ -> l.stroke = c("blue") }
    }

    protected abstract fun recalculateBounds()

    protected abstract fun getBounds(): List<Pair<Double, Double>>

    protected abstract fun refreshAsymptote()

    override fun plotData() {
        recalculateBounds()
        refreshXY()
        refreshAsymptote()
        if (data.isNotEmpty()) {
            refreshData()
        }
    }

}
