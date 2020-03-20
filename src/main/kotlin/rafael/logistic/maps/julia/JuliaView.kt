package rafael.logistic.maps.julia

import javafx.beans.binding.Bindings
import javafx.geometry.Point2D
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import rafael.logistic.view.GenerationStatus
import rafael.logistic.view.GenerationStatusChronometerListener
import rafael.logistic.view.configureMinMaxSpinners
import rafael.logistic.view.doubleSpinnerValueFactory
import rafael.logistic.view.mapchart.MouseRealPosNode
import rafael.logistic.view.view.ViewBase
import tornadofx.*


abstract class JuliaView(title: String, fxmlFile: String, generator: JuliaGenerator) : ViewBase<JuliaInfo, JuliaGenerator, JuliaCanvas>(title, fxmlFile, generator) {

    companion object {
        const val LIMIT = 2.0
    }

    // @formatter:off

    override val iterationsValueFactory :   SpinnerValueFactory<Int>
            = SpinnerValueFactory.ListSpinnerValueFactory(listOf(5, 10, 20, 30, 50, 100, 200, 300, 500).observable())

    private     val spnXMin             :   Spinner<Double>     by  fxid()
    private     val xMinValueFactory    =   doubleSpinnerValueFactory(-LIMIT, LIMIT, -LIMIT, 0.1)

    private     val spnXMax             :   Spinner<Double>     by  fxid()
    private     val xMaxValueFactory    =   doubleSpinnerValueFactory(-LIMIT, LIMIT, LIMIT, 0.1)

    private     val deltaXProperty      =   1.toProperty()
    private     val deltaXStepProperty  =   (0.1).toProperty()

    private     val spnYMin             :   Spinner<Double>     by  fxid()
    private     val yMinValueFactory    =   doubleSpinnerValueFactory(-LIMIT, LIMIT, -LIMIT, 0.1)

    private     val spnYMax             :   Spinner<Double>     by  fxid()
    private     val yMaxValueFactory    =   doubleSpinnerValueFactory(-LIMIT, LIMIT, LIMIT, 0.1)

    private     val deltaYProperty      =   1.toProperty()
    private     val deltaYStepProperty  =   (0.1).toProperty()

    private     val lblPosMouse         :   MouseRealPosNode    by  fxid()

    private     val lblStatus           :   Label               by  fxid()

    protected   val cXProperty          =   (0.0).toProperty()

    protected   val cYProperty          =   (0.0).toProperty()

    // @formatter:on


    override fun initializeControls() {
        configureMinMaxSpinners(spnXMin, xMinValueFactory, spnXMax, xMaxValueFactory,
                deltaXProperty, deltaXStepProperty, this::loadData)
        configureMinMaxSpinners(spnYMin, yMinValueFactory, spnYMax, yMaxValueFactory,
                deltaYProperty, deltaYStepProperty, this::loadData)
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

    override fun refreshData(generator: JuliaGenerator, iterations: Int): List<JuliaInfo> {
        return generator.generate(Point2D(0.0, 0.0), JuliaParameter(cXProperty.value,  cYProperty.value,
                spnXMin.value, spnXMax.value, chart.widthProperty().intValue(),
                spnYMin.value, spnYMax.value, chart.heightProperty().intValue()
        ), iterations)
    }

    override fun initializeAdditional() {
        spnIterations.valueFactory.value = 10

        lblPosMouse.bind(chart)
        super.generationStatusProperty().addListener(GenerationStatusChronometerListener())
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