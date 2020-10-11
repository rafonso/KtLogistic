package rafael.logistic.core.fx.iterationchart

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.PathElement
import tornadofx.*

class IterationChartDouble(
        @NamedArg("xAxis") xAxis: Axis<Int>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Int, Double>>) : IterationChartBase<Double>(xAxis, yAxis, data) {

    @Suppress("unused")
    constructor(@NamedArg("xAxis") xAxis: Axis<Int>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().asObservable<Series<Int, Double>>())

    override fun loadPath(iterationData: List<Double>): Array<PathElement> {
        val positions: List<Pair<Double, Double>> = iterationData
                .mapIndexed { i, d -> Pair(i.toIterationsXPos(), d.toIterationsYPos()) }

        return (
                listOf(MoveTo(positions.first().first, positions.first().second)) +
                        positions.subList(1, positions.size).map { LineTo(it.first, it.second) }
                )
                .toTypedArray()
    }

}
