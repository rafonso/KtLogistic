package rafael.logistic.bifurcation.ikeda

import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.view.ViewBase
import rafael.logistic.core.generation.GenerationStatus
import rafael.logistic.core.generation.GenerationStatusChronometerListener
import rafael.logistic.map.bifurcation.RData
import rafael.logistic.map.bifurcation.BifurcationCanvas
import tornadofx.*

class IkedaBifurcationApp: App(IkedaBifurcationView::class, Styles::class)

class IkedaBifurcationView : ViewBase<RData, IkedaBifurcationGenerator, BifurcationCanvas>(
    "Ikeda Bifurcarion",
    "IkedaBifurcation",
    IkedaBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                           : Spinner<Double>   by fxid()
    private val deltaX0Property                 = 1.toProperty()
    private val x0ValueFactory                  = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnX1                           : Spinner<Double>   by fxid()
    private val deltaX1Property                 = 1.toProperty()
    private val x1ValueFactory                  = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnSkip                         : Spinner<Int>      by fxid()
    private val skipValueFactory                = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0, 1)

    private val spnPixelsSeparation             : Spinner<Int>      by fxid()
    private val pixelsSeparationValueFactory    =
        SpinnerValueFactory.ListSpinnerValueFactory(listOf(0, 1, 2, 4, 10, 50, 100).asObservable())

    private val spnUMin                         : Spinner<Double>   by fxid()
    private val uMinValueFactory                = doubleSpinnerValueFactory(U_MIN, U_MAX, U_MIN, 0.1)

    private val spnUMax                         : Spinner<Double>   by fxid()
    private val uMaxValueFactory                = doubleSpinnerValueFactory(U_MIN, U_MAX, U_MAX, 0.1)

    private val deltaULimitProperty             = 1.toProperty()
    private val deltaUStepProperty              = (0.1).toProperty()

    private val lblPosMouse                     : MouseRealPosNode  by fxid()

    private val lblStatus                       : Label             by fxid()

    // @formatter:on

    override fun getImageName() = "lozi-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".X1=${x1ValueFactory.converter.toString(spnX1.value)}" +
            ".Iterations_U=${spnIterations.value}" +
            ".UMin=${uMinValueFactory.converter.toString(spnUMin.value)}" +
            ".UMax=${uMaxValueFactory.converter.toString(spnUMax.value)}" +
            (if (spnSkip.value > 0) ".Skip=${spnSkip.value}pct" else "") +
            (if (spnPixelsSeparation.value > 0) ".PxnSep=${spnPixelsSeparation.value}" else "")

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnX1.configureActions(x1ValueFactory, deltaX1Property, this::loadData)

        spnSkip.configureActions(skipValueFactory, this::loadData)

        pixelsSeparationValueFactory.value = 0
        spnPixelsSeparation.configureActions(pixelsSeparationValueFactory) {
            chart.pixelsSeparationProperty.value = spnPixelsSeparation.value + 1
            this.loadData()
        }

        configureMinMaxSpinners(
            spnUMin, uMinValueFactory, spnUMax, uMaxValueFactory,
            deltaULimitProperty, deltaUStepProperty, this::loadData
        )
    }

    override fun initializeCharts() {
        chart.yMinProperty.value = X_MIN
        chart.yMaxProperty.value = X_MAX

        val chartParent = chart.parent as Region
        chart.widthProperty().bind(chartParent.widthProperty())
        chart.widthProperty().onChange { loadData() }
        chart.heightProperty().bind(chartParent.heightProperty())
        chart.heightProperty().onChange { loadData() }

        chart.x0Property.bind(spnX0.valueProperty())

        chart.xMinProperty.bind(spnUMin.valueProperty())
        chart.xMaxProperty.bind(spnUMax.valueProperty())
    }

    override fun refreshData(generator: IkedaBifurcationGenerator, iterations: Int): List<RData> =
        if (chart.width > 0) {
            generator.generate(
                spnX1.value,
                super.chart.xMin, super.chart.xMax,
                super.chart.widthProperty().value.toInt() / (spnPixelsSeparation.value + 1),
                spnSkip.value, iterations,
                spnX0.value
            )
        } else emptyList()

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

}
