package rafael.logistic.view.gaussian

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.chart.NumberAxis
import javafx.scene.shape.Line
import rafael.logistic.generator.GaussianGenerator
import rafael.logistic.view.MapChart
import tornadofx.*

const val X_INTERVALS = 100

class GaussianChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChart(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    val alphaProperty = (0.0).toProperty()

    val betaProperty = (0.0).toProperty()

    private val deltaX = ((xAxis as NumberAxis).upperBound - (xAxis as NumberAxis).lowerBound) / X_INTERVALS

    private var oneNegativeX = -1.0
    private var oneNegativeY = -1.0
    private var onePositiveX = 1.0
    private var onePositiveY = 1.0

    override fun recalculateBounds() {
        oneNegativeX = (-1.0).toLogisticXPos()
        oneNegativeY = (-1.0).toLogisticYPos()
        onePositiveX = (1.0).toLogisticXPos()
        onePositiveY = (1.0).toLogisticYPos()
    }

    override fun refreshXY() {
        background.add(Line(oneNegativeX, oneNegativeY, onePositiveX, onePositiveY)
                .also { it.stroke = c("blue") }
        )
    }

    override fun refreshAsymptote() {
        val positions = (0..X_INTERVALS)
                .map { it * deltaX + (xAxis as NumberAxis).lowerBound }
                .map { x -> Pair(x, GaussianGenerator.calc(alphaProperty.value, betaProperty.value, x)) }
                .map { Pair(it.first.toLogisticXPos(), it.second.toLogisticYPos()) }
        (1 until positions.size)
                .map { i ->
                    Line(positions[i - 1].first, positions[i - 1].second, positions[i].first, positions[i].second)
                            .also { it.stroke = c("green") }
                }
                .forEach { background.add(it) }
    }

}