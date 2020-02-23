package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.Circle
import rafael.logistic.generator.BiPoint
import tornadofx.*

class IterationChartBi(
        @NamedArg("xAxis") xAxis: Axis<Int>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Int, Double>>) : IterationChartBase<BiPoint>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Int>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().observable())

    companion object {
        val extractorX: (BiPoint) -> Double = BiPoint::x
        val extractorY: (BiPoint) -> Double = BiPoint::y
    }

    private var extractor: (BiPoint) -> Double = { kotlin.error("") }

    fun bind(valueProperty: ReadOnlyObjectProperty<Int>, observableData: ObjectProperty<List<BiPoint>>, _extractor: (BiPoint) -> Double) {
        super.bind(valueProperty, observableData)
        this.extractor = _extractor
    }

    override fun refreshData() {
        super.iterationData
                .mapIndexed { i, pt -> Pair(i, extractor(pt)) }
                .filter { pair -> pair.second >= valueYAxis.lowerBound && pair.second <= valueYAxis.upperBound }
                .map { pair -> Triple(pair.first.toIterationsXPos(), pair.second.toIterationsYPos(), pair.first.toDouble() / super.iterationData.size) }
                .map { coords -> Circle(coords.first, coords.second, 2.0, getStroke(coords.third)) }
                .forEach { background.add(it) }
    }

}
