package rafael.logistic.map.set

import javafx.beans.binding.Bindings
import javafx.beans.binding.When
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.view.ViewBase
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.GenerationStatus
import rafael.logistic.core.generation.GenerationStatusChronometerListener
import tornadofx.*
import kotlin.math.abs


abstract class JuliaView(title: String, fxmlFile: String, generator: JuliaGenerator) : ViewBase<JuliaInfo, JuliaGenerator, JuliaCanvas>(title, fxmlFile, generator) {

    companion object {
        const val LIMIT = 2.0
    }

    // @formatter:off

    override val iterationsValueFactory :   SpinnerValueFactory<Int>
            = SpinnerValueFactory.ListSpinnerValueFactory(listOf(5, 10, 20, 30, 50, 100, 200, 300, 500, 1000).asObservable())

    protected   val spnXMin             :   Spinner<Double>     by  fxid()
    protected   val xMinValueFactory    =   doubleSpinnerValueFactory(-LIMIT, LIMIT, -LIMIT, 0.1)

    protected   val spnXMax             :   Spinner<Double>     by  fxid()
    protected   val xMaxValueFactory    =   doubleSpinnerValueFactory(-LIMIT, LIMIT, LIMIT, 0.1)

    private val deltaXProperty          =   1.toProperty()
    private val deltaXStepProperty      =   (0.1).toProperty()

    protected   val spnYMin             :   Spinner<Double>     by  fxid()
    protected   val yMinValueFactory    =   doubleSpinnerValueFactory(-LIMIT,
        LIMIT, -LIMIT, 0.1)

    protected   val spnYMax             :   Spinner<Double>     by  fxid()
    protected   val yMaxValueFactory    =   doubleSpinnerValueFactory(-LIMIT,
        LIMIT,
        LIMIT, 0.1)

    private     val deltaYProperty      =   1.toProperty()
    private     val deltaYStepProperty  =   (0.1).toProperty()

    private     val lblPosMouse         :   MouseRealPosNode    by  fxid()

    private     val lblStatus           :   Label               by  fxid()

    private val lblDeltaXY: Label by fxid()

    private val deltaXYProperty = (0.0).toProperty()

    private val deltaXYConverterProperty = objectProperty(yMinValueFactory.converterProperty().value)

    protected val cXProperty = (0.0).toProperty()

    protected   val cYProperty          =   (0.0).toProperty()

    // @formatter:on

    private fun recalculateDeltaXY() {
        deltaXYProperty.value = (spnXMax.value - spnXMin.value) - (spnYMax.value - spnYMin.value)
    }

    override fun initializeControls() {
        configureMinMaxSpinners(
            spnXMin, xMinValueFactory, spnXMax, xMaxValueFactory,
            deltaXProperty, deltaXStepProperty
        ) {
            this.recalculateDeltaXY()
            this.loadData()
        }
        configureMinMaxSpinners(
            spnYMin, yMinValueFactory, spnYMax, yMaxValueFactory,
            deltaYProperty, deltaYStepProperty
        ) {
            this.recalculateDeltaXY()
            this.loadData()
        }
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

//        chart.onMouseClicked = EventHandler { event ->
//            if(event.clickCount == 2) {
//                println(event)
//            }
//        }

    }

    override fun refreshData(generator: JuliaGenerator, iterations: Int): List<JuliaInfo> {
        return generator.generate(
            BiDouble.ZERO, JuliaParameter(
                cXProperty.value, cYProperty.value,
                spnXMin.value, spnXMax.value, chart.widthProperty().intValue(),
                spnYMin.value, spnYMax.value, chart.heightProperty().intValue()
            ), iterations
        )
    }

    override fun initializeAdditional() {
        spnIterations.valueFactory.value = 10

        deltaXYConverterProperty.bind(
            When(deltaXProperty.greaterThan(deltaYProperty)).then(xMinValueFactory.converterProperty())
                .otherwise(yMinValueFactory.converterProperty())
        )
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
