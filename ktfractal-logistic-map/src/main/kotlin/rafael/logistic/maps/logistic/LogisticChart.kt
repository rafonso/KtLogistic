package rafael.logistic.maps.logistic

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.paint.Color
import javafx.scene.shape.QuadCurve
import rafael.logistic.core.fx.mapchart.MapChartDouble
import tornadofx.*

class LogisticChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartDouble(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    val rProperty = (0.0).toProperty()

    private var zeroX =0.0
    private var zeroY =0.0
    private var halfX =0.0
    private var topY = 0.0
    private var oneX = 0.0
    private var oneY = 0.0

    override fun recalculateBounds() {
        zeroX = (0.0).realToChartX()
        zeroY = (0.0).realToChartY()
        halfX = (0.5).realToChartX()
        topY = (rProperty.value / 2).realToChartY()
        oneX = (1.0).realToChartX()
        oneY = (1.0).realToChartY()
    }

    override fun getBounds(): List<Pair<Double, Double>> = listOf(Pair(zeroX, zeroY), Pair(oneX, oneY))

    override fun refreshAsymptote() {
        background.add(QuadCurve(zeroX, zeroY, halfX, topY, oneX, zeroY)
                .also {
                    it.stroke = Color.GREEN
                    it.fill = Color.TRANSPARENT
                }
        )
    }

}
