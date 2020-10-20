package rafael.logistic.bifurcation.lozi

import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.view.ViewBase
import rafael.logistic.core.generation.GenerationStatus
import rafael.logistic.core.generation.GenerationStatusChronometerListener
import rafael.logistic.maps.bifurcation.RData
import rafael.logistic.maps.bifurcation.canvas.BifurcationCanvas
import tornadofx.asObservable
import tornadofx.onChange
import tornadofx.runLater
import tornadofx.toProperty

class LoziBifurcationView : ViewBase<RData, LoziBifurcationGenerator, BifurcationCanvas>(
    "Lozi Bifurcarion",
    "LoziBifurcation",
    LoziBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                           : Spinner<Double>   by fxid()
    private val deltaX0Property                 = 1.toProperty()
    private val x0ValueFactory                  = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnX1                           : Spinner<Double>   by fxid()
    private val deltaX1Property                 = 1.toProperty()
    private val x1ValueFactory                  = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnBeta                         : Spinner<Double>   by fxid()
    private val deltaBetaProperty               = 1.toProperty()
    private val betaValueFactory                = doubleSpinnerValueFactory(BETA_MIN, BETA_MAX, 0.0, 0.1)

    private val spnSkip                         : Spinner<Int>      by fxid()
    private val skipValueFactory                = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0, 1)

    private val spnPixelsSeparation             : Spinner<Int>      by fxid()
    private val pixelsSeparationValueFactory    =
        SpinnerValueFactory.ListSpinnerValueFactory(listOf(0, 1, 2, 4, 10, 50, 100).asObservable())

    private val spnAlphaMin                     : Spinner<Double>   by fxid()
    private val alphaMinValueFactory            = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, ALPHA_MIN, 0.1)

    private val spnAlphaMax                     : Spinner<Double>   by fxid()
    private val alphaMaxValueFactory            = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, ALPHA_MAX, 0.1)

    private val deltaAlphaLimitProperty         = 1.toProperty()
    private val deltaAlphaStepProperty          = (0.1).toProperty()

    private val lblPosMouse                     : MouseRealPosNode  by fxid()

    private val lblStatus                       : Label             by fxid()

    // @formatter:on

    override fun getImageName() = "lozi-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".X1=${x1ValueFactory.converter.toString(spnX1.value)}" +
            ".Beta=${betaValueFactory.converter.toString(spnBeta.value)}" +
            ".Iterations_Alpha=${spnIterations.value}" +
            ".AlphaMin=${alphaMinValueFactory.converter.toString(spnAlphaMin.value)}" +
            ".AlphaMax=${alphaMaxValueFactory.converter.toString(spnAlphaMax.value)}" +
            (if (spnSkip.value > 0) ".Skip=${spnSkip.value}pct" else "") +
            (if (spnPixelsSeparation.value > 0) ".PxnSep=${spnPixelsSeparation.value}" else "")

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnX1.configureActions(x1ValueFactory, deltaX1Property, this::loadData)
        spnBeta.configureActions(betaValueFactory, deltaBetaProperty, this::loadData)

        spnSkip.configureActions(skipValueFactory, this::loadData)

        pixelsSeparationValueFactory.value = 0
        spnPixelsSeparation.configureActions(pixelsSeparationValueFactory) {
            chart.pixelsSeparationProperty.value = spnPixelsSeparation.value + 1
            this.loadData()
        }

        configureMinMaxSpinners(
            spnAlphaMin, alphaMinValueFactory, spnAlphaMax, alphaMaxValueFactory,
            deltaAlphaLimitProperty, deltaAlphaStepProperty, this::loadData
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

        chart.xMinProperty.bind(spnAlphaMin.valueProperty())
        chart.xMaxProperty.bind(spnAlphaMax.valueProperty())
    }

    override fun refreshData(generator: LoziBifurcationGenerator, iterations: Int): List<RData> =
        if (chart.width > 0) {
            generator.generate(
                spnX1.value,
                super.chart.xMin, super.chart.xMax,
                super.chart.widthProperty().value.toInt() / (spnPixelsSeparation.value + 1),
                spnSkip.value, iterations,
                spnBeta.value, spnX0.value
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
