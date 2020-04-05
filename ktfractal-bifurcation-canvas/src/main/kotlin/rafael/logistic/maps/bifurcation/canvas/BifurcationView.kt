package rafael.logistic.maps.bifurcation.canvas

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
import rafael.logistic.maps.bifurcation.*
import tornadofx.asObservable
import tornadofx.onChange
import tornadofx.runLater
import tornadofx.toProperty

class BifurcationView : ViewBase<RData, BifurcationGenerator, BifurcationCanvas>(
    "Bifurcation Canvas",
    "BifurcationCanvas",
    LogisticBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0: Spinner<Double> by fxid()
    private val deltaX0Property = 1.toProperty()
    private val x0ValueFactory = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.5, 0.1)

    private val spnSkip: Spinner<Int> by fxid()
    private val skipValueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0, 1)

    private val spnPixelsSeparation: Spinner<Int> by fxid()
    private val pixelsSeparationValueFactory =
        SpinnerValueFactory.ListSpinnerValueFactory(listOf(0, 1, 2, 4, 10, 50, 100).asObservable())

    private val spnRMin: Spinner<Double> by fxid()
    private val rMinValueFactory = doubleSpinnerValueFactory(R_MIN, R_MAX, R_MIN, 0.1)


    private val spnRMax: Spinner<Double> by fxid()
    private val rMaxValueFactory = doubleSpinnerValueFactory(R_MIN, R_MAX, R_MAX, 0.1)

    private val deltaRLimitProperty = 1.toProperty()
    private val deltaRStepProperty = (0.1).toProperty()

    private val lblPosMouse: MouseRealPosNode by fxid()

    private val lblStatus: Label by fxid()

    // @formatter:on

    override fun getImageName() = "bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Iterations_R=${spnIterations.value}" +
            ".RMin=${rMinValueFactory.converter.toString(spnRMin.value)}" +
            ".RMax=${rMaxValueFactory.converter.toString(spnRMax.value)}" +
            (if (spnSkip.value > 0) ".Skip=${spnSkip.value}pct" else "") +
            (if (spnPixelsSeparation.value > 0) ".PxnSep=${spnPixelsSeparation.value}" else "")

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)

        spnSkip.configureActions(skipValueFactory, this::loadData)

        pixelsSeparationValueFactory.value = 0
        spnPixelsSeparation.configureActions(pixelsSeparationValueFactory) {
            chart.pixelsSeparationProperty.value = spnPixelsSeparation.value + 1
            this.loadData()
        }

        configureMinMaxSpinners(
            spnRMin, rMinValueFactory, spnRMax, rMaxValueFactory,
            deltaRLimitProperty, deltaRStepProperty, this::loadData
        )
    }

    override fun initializeCharts() {
        chart.yMinProperty.value = 0.0
        chart.yMaxProperty.value = 1.0

        val chartParent = chart.parent as Region
        chart.widthProperty().bind(chartParent.widthProperty())
        chart.widthProperty().onChange { loadData() }
        chart.heightProperty().bind(chartParent.heightProperty())
        chart.heightProperty().onChange { loadData() }

        chart.x0Property.bind(spnX0.valueProperty())

        chart.xMinProperty.bind(spnRMin.valueProperty())
        chart.xMaxProperty.bind(spnRMax.valueProperty())
    }

    override fun refreshData(generator: BifurcationGenerator, iterations: Int): List<RData> =
        if (chart.width > 0)
            generator.generate(
                spnX0.value, super.chart.xMin, super.chart.xMax,
                super.chart.widthProperty().value.toInt() / (spnPixelsSeparation.value + 1), spnSkip.value, iterations
            )
        else emptyList()

    override fun initializeAdditional() {
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
