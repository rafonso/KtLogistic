package rafael.logistic.bifurcation.gaussian

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

class GaussianBifurcationApp: App(GaussianBifurcationView::class, Styles::class)

class GaussianBifurcationView : ViewBase<RData, GaussianBifurcationGenerator, BifurcationCanvas>(
    "Gaussian Bifurcation",
    "GaussianBifurcation",
    GaussianBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                           : Spinner<Double>   by fxid()
    private val deltaX0Property                 = 1.toProperty()
    private val x0ValueFactory                  = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnAlpha                         : Spinner<Double>   by fxid()
    private val deltaAlphaProperty               = 1.toProperty()
    private val alphaValueFactory                = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, 5.0, 0.1)

    private val spnSkip                         : Spinner<Int>      by fxid()
    private val skipValueFactory                = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0, 1)

    private val spnPixelsSeparation             : Spinner<Int>      by fxid()
    private val pixelsSeparationValueFactory    =
        SpinnerValueFactory.ListSpinnerValueFactory(listOf(0, 1, 2, 4, 10, 50, 100).asObservable())

    private val spnBetaMin                     : Spinner<Double>   by fxid()
    private val betaMinValueFactory            = doubleSpinnerValueFactory(BETA_MIN, BETA_MAX, BETA_MIN, 0.1)

    private val spnBetaMax                     : Spinner<Double>   by fxid()
    private val betaMaxValueFactory            = doubleSpinnerValueFactory(BETA_MIN, BETA_MAX, BETA_MAX, 0.1)

    private val deltaBetaLimitProperty         = 1.toProperty()
    private val deltaBetaStepProperty          = (0.1).toProperty()

    private val lblPosMouse                     : MouseRealPosNode  by fxid()

    private val lblStatus                       : Label             by fxid()

    // @formatter:on

    override fun getImageName() = "gaussian-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Alpha=${alphaValueFactory.converter.toString(spnAlpha.value)}" +
            ".Iterations_Beta=${spnIterations.value}" +
            ".BetaMin=${betaMinValueFactory.converter.toString(spnBetaMin.value)}" +
            ".BetaMax=${betaMaxValueFactory.converter.toString(spnBetaMax.value)}" +
            (if (spnSkip.value > 0) ".Skip=${spnSkip.value}pct" else "") +
            (if (spnPixelsSeparation.value > 0) ".PxnSep=${spnPixelsSeparation.value}" else "")

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnAlpha.configureActions(alphaValueFactory, deltaAlphaProperty, this::loadData)

        spnSkip.configureActions(skipValueFactory, this::loadData)

        pixelsSeparationValueFactory.value = 0
        spnPixelsSeparation.configureActions(pixelsSeparationValueFactory) {
            chart.pixelsSeparationProperty.value = spnPixelsSeparation.value + 1
            this.loadData()
        }

        configureMinMaxSpinners(
            spnBetaMin, betaMinValueFactory, spnBetaMax, betaMaxValueFactory,
            deltaBetaLimitProperty, deltaBetaStepProperty, this::loadData
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

        chart.xMinProperty.bind(spnBetaMin.valueProperty())
        chart.xMaxProperty.bind(spnBetaMax.valueProperty())
    }

    override fun refreshData(generator: GaussianBifurcationGenerator, iterations: Int): List<RData> =
        if (chart.width > 0) {
            generator.generate(
                spnX0.value,
                super.chart.xMin, super.chart.xMax,
                super.chart.widthProperty().value.toInt() / (spnPixelsSeparation.value + 1),
                spnSkip.value, iterations,
                spnAlpha.value
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
