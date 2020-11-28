package rafael.logistic.map.gaussian

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import rafael.logistic.core.fx.zeroProperty
import rafael.logistic.map.fx.mapchart.MapChartDouble
import tornadofx.asObservable

const val X_INTERVALS = 100

class GaussianMapChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartDouble(xAxis, yAxis, data) {

    @Suppress("unused")
    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().asObservable<Series<Double, Double>>())

    val alphaProperty = zeroProperty()

    val betaProperty = zeroProperty()

    private val deltaX = ((xAxis as NumberAxis).upperBound - (xAxis as NumberAxis).lowerBound) / X_INTERVALS

    private var oneNegativeX = -1.0
    private var oneNegativeY = -1.0
    private var onePositiveX = 1.0
    private var onePositiveY = 1.0

    override fun recalculateBounds() {
        oneNegativeX = (-1.0).realToChartX()
        oneNegativeY = (-1.0).realToChartY()
        onePositiveX = (1.0).realToChartX()
        onePositiveY = (1.0).realToChartY()
    }

    override fun getBounds(): List<Pair<Double, Double>> =
            listOf(Pair(oneNegativeX, oneNegativeY), Pair(onePositiveX, onePositiveY))

    override fun refreshAsymptote() {
        val positions = (0..X_INTERVALS)
                .map { it * deltaX + (xAxis as NumberAxis).lowerBound }
                .map { x -> Pair(x, GaussianMapGenerator.calc(alphaProperty.value, betaProperty.value, x)) }
                .map { Pair(it.first.realToChartX(), it.second.realToChartY()) }
        plotLines(positions) { l, _ -> l.stroke = Color.GREEN }
    }

}
