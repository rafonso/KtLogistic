package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.Circle
import rafael.logistic.generator.BiPoint
import tornadofx.*

class IteractionChartBi(
        @NamedArg("xAxis") xAxis: Axis<Int>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Int, Double>>) : IteractionChartBase<BiPoint>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Int>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().observable())

    companion object {
        val extractorX: (BiPoint) -> Double = BiPoint::x
        val extractorY: (BiPoint) -> Double = BiPoint::y
    }

    private var extractor: (BiPoint) -> Double = { kotlin.error("") }

    fun bind(valueProperty: ReadOnlyObjectProperty<Int>, observablData: ObjectProperty<List<BiPoint>>, _extractor: (BiPoint) -> Double) {
        super.bind(valueProperty, observablData)
        this.extractor = _extractor
    }

    override fun refreshData() {
        super.iteractionData
                .mapIndexed { i, pt -> Pair(i, extractor(pt)) }
                .filter { pair -> pair.second >= valueYAxis.lowerBound && pair.second <= valueYAxis.upperBound }
                .map { pair -> Triple(pair.first.toIteractionsXPos(), pair.second.toIteractionsYPos(), pair.first.toDouble() / super.iteractionData.size) }
                .map { coord -> Circle(coord.first, coord.second, 2.0, getStroke(coord.third)) }
                .forEach { background.add(it) }
    }

}