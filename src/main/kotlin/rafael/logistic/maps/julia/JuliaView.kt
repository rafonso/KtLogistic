package rafael.logistic.maps.julia

import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import rafael.logistic.view.*
import rafael.logistic.view.mapchart.MouseRealPosNode
import rafael.logistic.view.view.ViewBase
import tornadofx.*

const val LIMIT = 2.0

class JuliaView : ViewBase<JuliaInfo, JuliaGenerator, JuliaCanvas>("Julia", "Julia", JuliaGenerator()) {

    // @formatter:off

    override val iterationsValueFactory :   SpinnerValueFactory<Int>
            = SpinnerValueFactory.ListSpinnerValueFactory(listOf(1, 2, 5, 10, 20, 30).observable())

    private     val spnZ0X              :   Spinner<Double>     by  fxid()
    private     val deltaZ0XProperty    =   1.toProperty()
    private     val z0XValueFactory     =   doubleSpinnerValueFactory(-LIMIT, LIMIT, 0.0, 0.1)

    private     val spnZ0Y              :   Spinner<Double>     by  fxid()
    private     val deltaZ0YProperty    =   1.toProperty()
    private     val z0YValueFactory     =   doubleSpinnerValueFactory(-LIMIT, LIMIT, 0.0, 0.1)

    private     val spnCX               :   Spinner<Double>     by  fxid()
    private     val deltaCXProperty     =   1.toProperty()
    private     val cXValueFactory      =   doubleSpinnerValueFactory(-LIMIT, LIMIT, LIMIT / 2, 0.1)

    private     val spnCY               :   Spinner<Double>     by  fxid()
    private     val deltaCYProperty     =   1.toProperty()
    private     val cYValueFactory      =   doubleSpinnerValueFactory(-LIMIT, LIMIT, LIMIT / 2, 0.1)

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

    private     val btnMandelbrot       :   Button              by  fxid()

    private     val lblPosMouse         :   MouseRealPosNode    by  fxid()

    private     val lblStatus           :   Label               by  fxid()



    private     val z0Property          =   Point2D(0.0, 0.0).toProperty()
    private     var z0                  by  z0Property

    private     val cProperty           =   Point2D(0.0, 0.0).toProperty()
    private     var c                   by  cProperty

    // @formatter:on


    override fun initializeControls() {
        spnZ0X.configureActions(z0XValueFactory, deltaZ0XProperty, this::loadData)
        spnZ0X.valueProperty().onChange { x -> this.z0 = Point2D(x!!, z0.y) }

        spnZ0Y.configureActions(z0YValueFactory, deltaZ0YProperty, this::loadData)
        spnZ0Y.valueProperty().onChange { y -> this.z0 = Point2D(z0.x, y!!) }

        spnCX.configureActions(cXValueFactory, deltaCXProperty, this::loadData)
        spnCX.valueProperty().onChange { x -> this.c = Point2D(x!!, c.y) }

        spnCY.configureActions(cYValueFactory, deltaCYProperty, this::loadData)
        spnCY.valueProperty().onChange { y -> this.c = Point2D(c.x, y!!) }

        configureMinMaxSpinners(spnXMin, xMinValueFactory, spnXMax, xMaxValueFactory,
                deltaXProperty, deltaXStepProperty, this::loadData)
        configureMinMaxSpinners(spnYMin, yMinValueFactory, spnYMax, yMaxValueFactory,
                deltaYProperty, deltaYStepProperty, this::loadData)

        btnMandelbrot.onAction = EventHandler {
            z0XValueFactory.value = 0.0
            z0YValueFactory.value = 0.0
        }
    }

    override fun initializeCharts() {
        chart.backgroundProperty.value = Color.BLACK

        val chartParent = chart.parent as Region
        chart.widthProperty().bind(chartParent.widthProperty())
        chart.widthProperty().onChange { loadData() }
        chart.heightProperty().bind(chartParent.heightProperty())
        chart.heightProperty().onChange { loadData() }

        chart.xMinProperty.bind(spnXMin.valueProperty())
        chart.xMaxProperty.bind(spnXMax.valueProperty())
        chart.yMinProperty.bind(spnYMin.valueProperty())
        chart.yMaxProperty.bind(spnYMax.valueProperty())

        chart.maxIterationsProperty.bind(super.spnIterations.valueProperty())
    }

    override fun refreshData(generator: JuliaGenerator, iterations: Int): List<JuliaInfo> {
        return generator.generate(z0, JuliaParameter(c,
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