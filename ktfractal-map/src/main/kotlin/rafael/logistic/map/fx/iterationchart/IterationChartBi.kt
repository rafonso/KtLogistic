package rafael.logistic.map.fx.iterationchart

import javafx.beans.NamedArg
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.PathElement
import rafael.logistic.core.generation.BiDouble
import tornadofx.*

class IterationChartBi(
        @NamedArg("xAxis") xAxis: Axis<Int>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Int, Double>>) : IterationChartBase<BiDouble>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Int>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().asObservable<Series<Int, Double>>())

    companion object {
        val extractorX: (BiDouble) -> Double = BiDouble::x
        val extractorY: (BiDouble) -> Double = BiDouble::y
    }

    private var extractor: (BiDouble) -> Double = { kotlin.error("") }

    internal fun bind(valueProperty: ReadOnlyObjectProperty<Int>, observableData: ReadOnlyObjectProperty<List<BiDouble>>, _extractor: (BiDouble) -> Double) {
        super.bind(valueProperty, observableData)
        this.extractor = _extractor
    }

    override fun loadPath(iterationData: List<BiDouble>): Array<PathElement> {
        val positions: List<Pair<Int, Double>> = iterationData
                .mapIndexed { i, pt -> Pair(i, extractor(pt)) }
                .filter { pair -> pair.second >= valueYAxis.lowerBound && pair.second <= valueYAxis.upperBound }

        return positions.indices
                .map { index -> Triple(index == 0 || positions[index].first - positions[index - 1].first > 1, positions[index].first.toIterationsXPos(), positions[index].second.toIterationsYPos()) }
                .map { t -> if (t.first) MoveTo(t.second, t.third) else LineTo(t.second, t.third) }
                .toTypedArray()
    }

}
