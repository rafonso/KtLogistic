package rafael.logistic.map.fx.iterationchart

import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.PathElement
import javafx.util.StringConverter
import rafael.logistic.core.fx.CONVERTER_2
import rafael.logistic.core.fx.LogisticConverter
import rafael.logistic.core.fx.rainbowColors
import rafael.logistic.core.generation.GenerationStatus
import tornadofx.*

@Suppress("UNCHECKED_CAST")
val CONVERTER_0 = LogisticConverter(0) as StringConverter<Number>

abstract class IterationChartBase<T>(
    xAxis: Axis<Int>,
    yAxis: Axis<Double>,
    data: ObservableList<Series<Int, Double>>
) : LineChart<Int, Double>(xAxis, yAxis, data) {

    private val background: Node = super.lookup(".chart-plot-background")

    private val iterationDataProperty = emptyList<T>().toProperty()

    private val iterationsProperty = 0.toProperty()

    private val valueXAxis = (xAxis as NumberAxis)

    protected val valueYAxis = (yAxis as NumberAxis)

    private val gradient = LinearGradient(0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE,
        rainbowColors.mapIndexed { i, color -> Stop(i / 6.0, color) })

    init {
        valueXAxis.tickLabelFormatter = CONVERTER_0
        valueYAxis.tickLabelFormatter = CONVERTER_2
        iterationsProperty.onChange {
            valueXAxis.upperBound = it.toDouble()
        }
        valueXAxis.tickUnitProperty().bind(valueXAxis.upperBoundProperty().divide(10))
        iterationDataProperty.onChange {
            layoutPlotChildren()
        }
    }

    protected fun Int.toIterationsXPos() = xAxis.getDisplayPosition(this)

    protected fun Double.toIterationsYPos() = valueYAxis.getDisplayPosition(this)

    protected abstract fun loadPath(iterationData: List<T>): Array<PathElement>

    /**
     * Vincula a atualização do gráfico a quando o status do gráfico principal chegar
     * ao [status][GenerationStatus] [GenerationStatus.PLOTTING_FINALIZING].
     *
     * @param statusProperty properiedade do status do gráfico principal
     * @param iterationsProperty Propriedade relacionada a quantidade de iterações
     * @param dataProperty Propriedade relacionada aos dados brutos.
     */
    internal open fun bind(
        statusProperty: ObservableValue<GenerationStatus>,
        iterationsProperty: ObservableValue<Int>,
        dataProperty: ObservableValue<List<T>>
    ) {
        statusProperty.onChange { status ->
            if (status == GenerationStatus.PLOTTING_FINALIZING) {
                this.iterationsProperty.value = iterationsProperty.value
                this.iterationDataProperty.value = dataProperty.value
            }
        }
    }

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()

        if (iterationDataProperty.value.isNotEmpty()) {
            val elements = loadPath(iterationDataProperty.value)
            background.add(path(*elements) {
                this.stroke = gradient
            })
        }
    }

}
