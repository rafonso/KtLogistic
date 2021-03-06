package rafael.logistic.map.fx.mapchart

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import tornadofx.*

private const val MAX_WIDTH = 1.0
private const val MIN_WIDTH = 0.4
private const val DELTA_WIDTH = MAX_WIDTH - MIN_WIDTH

abstract class MapChartDouble(
    @NamedArg("xAxis") xAxis: Axis<Double>,
    @NamedArg("yAxis") yAxis: Axis<Double>,
    @NamedArg("data") data: ObservableList<Series<Double, Double>>
) : MapChartBase<Double>(xAxis, yAxis, data) {

    private fun refreshXY() {
        plotLines(getBounds()) { l, _ -> l.stroke = Color.BLUE }
    }

    private fun indexToLine(i: Int, coords: List<Pair<Double, Double>>, handler: (Line, Int) -> Unit) =
        Line(coords[i - 1].first, coords[i - 1].second, coords[i].first, coords[i].second).also { l ->
            handler(l, i)
        }

    protected fun plotLines(coords: List<Pair<Double, Double>>, handler: (Line, Int) -> Unit = { _, _ -> }) {
        val elements = coordinatesToLines(coords, handler)

        background.getChildList()?.addAll(elements)
    }

    private fun coordinatesToLines(coords: List<Pair<Double, Double>>, handler: (Line, Int) -> Unit): Array<Node> =
        (1 until coords.size)
            .toList()
            .parallelStream()
            .map { indexToLine(it, coords, handler) }
            .toArray { length -> arrayOfNulls(length) }

    protected abstract fun recalculateBounds()

    protected abstract fun getBounds(): List<Pair<Double, Double>>

    protected abstract fun refreshAsymptote()

    override fun initialize() {
        super.prefWidthProperty().bindBidirectional(super.prefHeightProperty())
    }

    override fun prepareBackground(data0: List<Double>) {
        background.getChildList()?.clear()
        recalculateBounds()
        refreshXY()
        refreshAsymptote()
        // Destaca o x0 e y0
        highlightP0(data0.first(), 0.0)
    }

    override fun dataToElementsToPlot(data0: List<Double>): Array<Node> {
        val coords = (listOf(Pair(data0[0], 0.0)) + (1 until data0.size)
            .flatMap { i -> listOf(Pair(data0[i - 1], data0[i]), Pair(data0[i], data0[i])) })
            .map { (x, y) -> Pair(x.realToChartX(), y.realToChartY()) }

        return coordinatesToLines(coords) { l, i ->
            l.stroke = colorCache.get(i, coords.size)
            l.strokeWidth = (DELTA_WIDTH * i / coords.size + MIN_WIDTH)
            l.opacity = 0.5
        }
    }

    override fun plotData(element: Array<Node>) {
        background.getChildList()?.addAll(element)
    }

}

