package rafael.logistic.view.tent

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.Line
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import rafael.logistic.view.MapChart
import tornadofx.*

class TentChart(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChart(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    val miProperty = (0.0).toProperty()

    private var zeroY = 0.0
    private var zeroX = 0.0
    private var halfX = 0.0
    private var topY = 0.0
    private var oneX = 0.0
    private var oneY = 0.0

    override fun recalculateBounds() {
        zeroY = (0.0).toLogisticYPos()
        zeroX = (0.0).toLogisticXPos()
        halfX = (0.5).toLogisticXPos()
        topY = (miProperty.value / 2).toLogisticYPos()
        oneX = (1.0).toLogisticXPos()
        oneY = (1.0).toLogisticYPos()
    }

    override fun refreshXY() {
        background.add(Line(zeroX, zeroY, oneX, oneY)
                .also { it.stroke = c("blue") }
        )
    }

    override fun refreshAsymptote() {
        background.add(Path(MoveTo(zeroX, zeroY), LineTo(halfX, topY), LineTo(oneX, zeroY))
                .also { it.stroke = c("green") }
        )
    }

}