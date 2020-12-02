package rafael.logistic.bifurcation

import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import rafael.logistic.core.fx.*
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.view.ViewBase
import rafael.logistic.core.generation.GenerationStatus
import rafael.logistic.core.generation.GenerationStatusChronometerListener
import tornadofx.observableListOf
import tornadofx.onChange
import tornadofx.runLater

abstract class BifurcationView<G : BifurcationGenerator<*>> protected constructor(
    title: String,
    fxmlFile: String,
    generator: G
) :
    ViewBase<RData, G, BifurcationCanvas>(title, fxmlFile, generator) {

    // @formatter:off

    private val spnSkip                         : Spinner<Int>      by fxid()
    private val skipValueFactory                = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0, 1)
    protected val skip                          : Int
        get() = spnSkip.value

    private val spnPixelsSeparation             : Spinner<Int>      by fxid()
    private val pixelsSeparationValueFactory    =
        SpinnerValueFactory.ListSpinnerValueFactory(observableListOf(0, 1, 2, 4, 10, 50, 100))
    private val pixelsSeparation                : Int
        get() = spnPixelsSeparation.value

    private val lblPosMouse                     : MouseRealPosNode  by fxid()

    private val lblStatus                       : Label             by fxid()

    protected abstract val xAxisConfiguration   : LimitsSpinnersConfiguration

    protected abstract val yAxisConfiguration   : LimitsSpinnersConfiguration

    // @formatter:on

    protected abstract fun getParametersName(): String

    override fun getImageName() = getParametersName() +
            (if (skip > 0) ".Skip=${skip}pct" else "") +
            (if (pixelsSeparation > 0) ".PxnSep=${pixelsSeparation}" else "")

    override fun initializeControls() {
        spnSkip.configureActions(skipValueFactory, this::loadData)

        configureMinMaxSpinners(xAxisConfiguration, this::loadData)
        configureMinMaxSpinners(yAxisConfiguration, this::loadData)

        pixelsSeparationValueFactory.value = 0
        spnPixelsSeparation.configureActions(pixelsSeparationValueFactory) {
            chart.pixelsSeparationProperty.value = spnPixelsSeparation.value + 1
            this.loadData()
        }
    }

    override fun initializeAdditional() {
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

    protected fun initializeCharts(
        spnX0: Spinner<Double>,
        spnXMin: Spinner<Double>,
        spnXMax: Spinner<Double>,
        spnYMin: Spinner<Double>,
        spnYMax: Spinner<Double>
    ) {
        val chartParent = chart.parent as Region
        chart.widthProperty().bind(chartParent.widthProperty())
        chart.widthProperty().onChange { loadData() }
        chart.heightProperty().bind(chartParent.heightProperty())
        chart.heightProperty().onChange { loadData() }

        chart.x0Property.bind(spnX0.valueProperty())

        chart.xMinProperty.bind(spnXMin.valueProperty())
        chart.xMaxProperty.bind(spnXMax.valueProperty())
        chart.yMinProperty.bind(spnYMin.valueProperty())
        chart.yMinProperty.onChange { chart.refreshData() }
        chart.yMaxProperty.bind(spnYMax.valueProperty())
        chart.yMaxProperty.onChange { chart.refreshData() }
    }

    protected abstract fun refreshData(generator: G, iterations: Int, stepsForR: Int, skip: Int): List<RData>

    override fun refreshData(generator: G, iterations: Int): List<RData> =
        if (chart.width > 0) refreshData(
            generator,
            iterations,
            super.chart.widthProperty().value.toInt() / (pixelsSeparation + 1),
            skip
        )
        else emptyList()

}
