package rafael.logistic.view.mapchart

import javafx.beans.NamedArg
import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.shape.HLineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.VLineTo
import rafael.logistic.view.getStroke
import rafael.logistic.view.plotLines
import tornadofx.*
import kotlin.reflect.KFunction0

abstract class MapChartDouble(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : MapChartBase<Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    private val plotterProperty: ObjectProperty<KFunction0<Unit>> = (::plotWithLines).toProperty().also {
        it.onChange { layoutPlotChildren() }
    }
    private var plotter by plotterProperty

    private val plotLineProperty = true.toProperty().also {
        it.onChange { useLines ->
            if (useLines) {
                plotter = ::plotWithLines
            } else {
                plotter = ::plotWithPath
            }
        }
    }

    private fun plotWithLines() {
        val coords = (listOf(Pair(data[0], 0.0)) + (1 until data.size)
                .flatMap { i -> listOf(Pair(data[i - 1], data[i]), Pair(data[i], data[i])) })
                .map { (x, y) -> Pair(x.toLogisticXPos(), y.toLogisticYPos()) }

        plotLines(coords, background) { l, i ->
            l.stroke = getStroke(i.toDouble() / coords.size)
            l.strokeWidth = (1.6 * i / coords.size + 0.4)
        }
    }

    private fun plotWithPath() {
        val elements = (
                listOf(MoveTo(data[0].toLogisticXPos(), (0.0).toLogisticYPos())) +
                        data.subList(1, data.size).flatMap { listOf(VLineTo(it.toLogisticYPos()), HLineTo(it.toLogisticXPos())) }
                ).toTypedArray()

        background.add(path(*elements) {
            this.stroke = Color.RED
            this.opacity = 0.5
        })
    }

    private fun refreshXY() {
        plotLines(getBounds(), background) { l, _ -> l.stroke = Color.BLUE }
    }

    protected abstract fun recalculateBounds()

    protected abstract fun getBounds(): List<Pair<Double, Double>>

    protected abstract fun refreshAsymptote()

    override fun initialize() {
        setOnMouseClicked { event ->
            if (event.button == MouseButton.PRIMARY && event.clickCount == 2) {
                plotLineProperty.value = !plotLineProperty.value
            }
        }
    }

    override fun plotData() {
        recalculateBounds()
        refreshXY()
        refreshAsymptote()
        if (data.isNotEmpty()) {
            // Destaca o x0 e y0
            highlightP0(data.first(), 0.0)
            plotter()
        }
    }

}

