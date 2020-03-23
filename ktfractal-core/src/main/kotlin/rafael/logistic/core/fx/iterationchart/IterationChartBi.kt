package rafael.logistic.core.fx.iterationchart

import javafx.beans.NamedArg
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.PathElement
import javafx.geometry.Point2D
import tornadofx.*

class IterationChartBi(
        @NamedArg("xAxis") xAxis: Axis<Int>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Int, Double>>) : IterationChartBase<Point2D>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Int>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().observable())

    companion object {
        val extractorX: (Point2D) -> Double = Point2D::getX
        val extractorY: (Point2D) -> Double = Point2D::getY
    }

    private var extractor: (Point2D) -> Double = { kotlin.error("") }

    fun bind(valueProperty: ReadOnlyObjectProperty<Int>, observableData: ReadOnlyObjectProperty<List<Point2D>>, _extractor: (Point2D) -> Double) {
        super.bind(valueProperty, observableData)
        this.extractor = _extractor
    }

    override fun loadPath(iterationData: List<Point2D>): Array<PathElement> {
        val positions: List<Pair<Int, Double>> = iterationData
                .mapIndexed { i, pt -> Pair(i, extractor(pt)) }
                .filter { pair -> pair.second >= valueYAxis.lowerBound && pair.second <= valueYAxis.upperBound }

        return positions.indices
                .map { index -> Triple(index == 0 || positions[index].first - positions[index - 1].first > 1, positions[index].first.toIterationsXPos(), positions[index].second.toIterationsYPos()) }
                .map { t -> if (t.first) MoveTo(t.second, t.third) else LineTo(t.second, t.third) }
                .toTypedArray()
    }

}
