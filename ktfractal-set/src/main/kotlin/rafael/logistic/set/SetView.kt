package rafael.logistic.set

import javafx.beans.binding.Bindings
import javafx.scene.control.Label
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import rafael.logistic.core.fx.DoubleSpinner
import rafael.logistic.core.fx.LimitsSpinnersConfiguration
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.view.ViewBase
import rafael.logistic.core.fx.zeroProperty
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.GenerationStatus
import rafael.logistic.core.generation.GenerationStatusChronometerListener
import tornadofx.asObservable
import tornadofx.onChange
import tornadofx.runLater
import kotlin.math.abs


abstract class SetView(title: String, fxmlFile: String, generator: SetGenerator) :
    ViewBase<SetInfo, SetGenerator, SetCanvas>(title, fxmlFile, generator) {

    companion object {
        const val LIMIT = 2.0
    }

    // @formatter:off

    override val iterationsValueFactory         :   SpinnerValueFactory<Int>
            = SpinnerValueFactory.ListSpinnerValueFactory(listOf(5, 10, 20, 30, 50, 100, 200, 300, 500, 1000).asObservable())

    protected   val spnXMin                     :   DoubleSpinner     by  fxid()
    protected   val spnXMax                     :   DoubleSpinner     by  fxid()
    protected   val spnYMin                     :   DoubleSpinner     by  fxid()
    protected   val spnYMax                     :   DoubleSpinner     by  fxid()

    private     val lblPosMouse                 :   MouseRealPosNode    by  fxid()
    private     val lblStatus                   :   Label               by  fxid()
    private     val lblDeltaXY                  :   Label               by  fxid()

    private     val deltaXYProperty             =   zeroProperty()

    protected   val cXProperty                  =   zeroProperty()
    protected   val cYProperty                  =   zeroProperty()

    // @formatter:on

    private fun reload() {
        deltaXYProperty.value = (spnXMax.value - spnXMin.value) - (spnYMax.value - spnYMin.value)
        this.loadData()
    }

    override fun initializeControls() {
        configureMinMaxSpinners(LimitsSpinnersConfiguration(spnXMin, spnXMax, -LIMIT, LIMIT), this::reload)
        configureMinMaxSpinners(LimitsSpinnersConfiguration(spnYMin, spnYMax, -LIMIT, LIMIT), this::reload)
    }

    override fun initializeCharts() {
        chart.backgroundProperty.value = Color.BLACK

        chart.heightProperty().bind(chart.widthProperty())
        val chartParent = chart.parent as Region
        chart.widthProperty().bind(Bindings.min(chartParent.widthProperty(), chartParent.heightProperty()))
        chart.widthProperty().onChange { loadData() }
        chart.heightProperty().onChange { loadData() }

        chart.xMinProperty.bind(spnXMin.valueProperty())
        chart.xMaxProperty.bind(spnXMax.valueProperty())
        chart.yMinProperty.bind(spnYMin.valueProperty())
        chart.yMaxProperty.bind(spnYMax.valueProperty())

        chart.maxIterationsProperty.bind(super.spnIterations.valueProperty())
    }

    override fun refreshData(generator: SetGenerator, iterations: Int): List<SetInfo> {
        val parameter = SetParameter(
            cXProperty.value, cYProperty.value,
            spnXMin.value, spnXMax.value, chart.widthProperty().intValue(),
            spnYMin.value, spnYMax.value, chart.heightProperty().intValue()
        )

        return generator.generate(BiDouble.ZERO, parameter, iterations)
    }

    override fun initializeAdditional() {
        spnIterations.valueFactory.value = 10

        lblDeltaXY.textProperty()
            .bind(Bindings.concat("ΔX - ΔY = ", deltaXYProperty.asString("%+.10f")))
        deltaXYProperty.onChange { delta ->
            lblDeltaXY.textFill =
                if (delta > 0) Color.color(0.0, deltaXYProperty.value / 4, 0.0)
                else Color.color(-deltaXYProperty.value / 4, 0.0, 0.0)

            val isZero = abs(delta) < 0.000000001
            lblDeltaXY.style = "-fx-background-color: ${if (isZero) "green" else "transparent"};"
        }

        lblPosMouse.bind(chart)
        GenerationStatusChronometerListener.bind(super.generationStatusProperty())
        super.generationStatusProperty().onChange {
            runLater {
                it?.let { status ->
                    lblStatus.text = if (status == GenerationStatus.IDLE) "" else status.toString()
                }
            }
        }
        chart.refreshData()
    }

}
