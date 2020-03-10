package rafael.logistic.view.mapchart

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import rafael.logistic.view.getRainbowColor
import tornadofx.*
import java.util.stream.Collectors

private const val MAX_WIDTH = 1.0
private const val MIN_WIDTH = 0.4
private const val DELTA_WIDTH = MAX_WIDTH - MIN_WIDTH

abstract class MapChartDouble(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartBase<Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

//    val x0Property = square.xProperty
//    var x0 by x0Property

    private fun plotWithLines() {
        val coords = (listOf(Pair(data[0], 0.0)) + (1 until data.size)
                .flatMap { i -> listOf(Pair(data[i - 1], data[i]), Pair(data[i], data[i])) })
                .map { (x, y) -> Pair(x.realToChartX(), y.realToChartY()) }

        plotLines(coords) { l, i ->
            l.stroke = getRainbowColor(i.toDouble() / coords.size)
            l.strokeWidth = (DELTA_WIDTH * i / coords.size + MIN_WIDTH)
            l.opacity = 0.5
        }
    }

    private fun refreshXY() {
        plotLines(getBounds()) { l, _ -> l.stroke = Color.BLUE }
    }

    protected fun plotLines(coords: List<Pair<Double, Double>>, handler: (Line, Int) -> Unit = { _, _ -> }) {
        val elements = (1 until coords.size)
                .toList()
                .parallelStream()
                .map { i ->
                    Line(coords[i - 1].first, coords[i - 1].second, coords[i].first, coords[i].second).also { l ->
                        handler(l, i)
                    }
                }
                .collect(Collectors.toList())

        background.getChildList()?.addAll(elements)
    }

    protected abstract fun recalculateBounds()

    protected abstract fun getBounds(): List<Pair<Double, Double>>

    protected abstract fun refreshAsymptote()

    override fun plotData() {
        recalculateBounds()
        refreshXY()
        refreshAsymptote()
        if (data.isNotEmpty()) {
            // Destaca o x0 e y0
            highlightP0(data.first(), 0.0)
            plotWithLines()
        }
    }

//    override fun p0Moved(square: Point0, pos: Point2D) {
//        if(pos.x >= 0 && pos.x <= background.layoutBounds.maxX) {
//            square.x = pos.x
//        }
//    }

}

