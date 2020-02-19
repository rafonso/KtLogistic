package rafael.logistic.view.gaussian

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.chart.NumberAxis
import rafael.logistic.generator.GaussianGenerator
import rafael.logistic.view.MapChart
import rafael.logistic.view.plotLines
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

    override fun getBounds(): List<Pair<Double, Double>> =
            listOf(Pair(oneNegativeX, oneNegativeY), Pair(onePositiveX, onePositiveY))

    override fun refreshAsymptote() {
        val positions = (0..X_INTERVALS)
                .map { it * deltaX + (xAxis as NumberAxis).lowerBound }
                .map { x -> Pair(x, GaussianGenerator.calc(alphaProperty.value, betaProperty.value, x)) }
                .map { Pair(it.first.toLogisticXPos(), it.second.toLogisticYPos()) }
        plotLines(positions, background) { l, _ -> l.stroke = c("green") }
    }

}