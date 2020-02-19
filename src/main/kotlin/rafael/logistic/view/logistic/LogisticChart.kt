package rafael.logistic.view.logistic

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.QuadCurve
import rafael.logistic.view.MapChart
import tornadofx.*

class LogisticChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChart(xAxis, yAxis, data) {

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
        zeroX = (0.0).toLogisticXPos()
        zeroY = (0.0).toLogisticYPos()
        halfX = (0.5).toLogisticXPos()
        topY = (rProperty.value / 2).toLogisticYPos()
        oneX = (1.0).toLogisticXPos()
        oneY = (1.0).toLogisticYPos()
    }

    override fun getBounds(): List<Pair<Double, Double>> = listOf(Pair(zeroX, zeroY), Pair(oneX, oneY))

    override fun refreshAsymptote() {
        background.add(QuadCurve(zeroX, zeroY, halfX, topY, oneX, zeroY)
                .also {
                    it.stroke = c("green")
                    it.fill = c("transparent")
                }
        )
    }

}