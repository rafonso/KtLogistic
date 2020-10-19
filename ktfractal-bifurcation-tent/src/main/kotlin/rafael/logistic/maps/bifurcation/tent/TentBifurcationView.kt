package rafael.logistic.maps.bifurcation.tent

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
import rafael.logistic.maps.bifurcation.canvas.BifurcationCanvas
import tornadofx.asObservable
import tornadofx.onChange
import tornadofx.runLater
import tornadofx.toProperty

class TentBifurcationView : ViewBase<RData, TentBifurcationGenerator, BifurcationCanvas>(
    "Tent Bifurcation",
    "TentBifurcation",
    TentBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0: Spinner<Double> by fxid()
    private val deltaX0Property = 1.toProperty()
    private val x0ValueFactory = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.5, 0.1)

    private val spnSkip: Spinner<Int> by fxid()
    private val skipValueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 90, 0, 1)

    private val spnPixelsSeparation: Spinner<Int> by fxid()
    private val pixelsSeparationValueFactory =
        SpinnerValueFactory.ListSpinnerValueFactory(listOf(0, 1, 2, 4, 10, 50, 100).asObservable())

    private val spnMiMin: Spinner<Double> by fxid()
    private val miMinValueFactory = doubleSpinnerValueFactory(MI_MIN, MI_MAX, MI_MIN, 0.1)


    private val spnMiMax: Spinner<Double> by fxid()
    private val miMaxValueFactory = doubleSpinnerValueFactory(MI_MIN, MI_MAX, MI_MAX, 0.1)

    private val deltaMiLimitProperty = 1.toProperty()
    private val deltaMiStepProperty = (0.1).toProperty()

    private val lblPosMouse: MouseRealPosNode by fxid()

    private val lblStatus: Label by fxid()

    // @formatter:on

    override fun getImageName() = "tent-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Iterations_R=${spnIterations.value}" +
            ".RMin=${miMinValueFactory.converter.toString(spnMiMin.value)}" +
            ".RMax=${miMaxValueFactory.converter.toString(spnMiMax.value)}" +
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
            spnMiMin, miMinValueFactory, spnMiMax, miMaxValueFactory,
            deltaMiLimitProperty, deltaMiStepProperty, this::loadData
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

        chart.xMinProperty.bind(spnMiMin.valueProperty())
        chart.xMaxProperty.bind(spnMiMax.valueProperty())
    }

    override fun refreshData(generator: TentBifurcationGenerator, iterations: Int): List<RData> =
        if (chart.width > 0)
            generator.generate(
                spnX0.value, super.chart.xMin, super.chart.xMax,
                super.chart.widthProperty().value.toInt() / (spnPixelsSeparation.value + 1), spnSkip.value, iterations
            )
        else emptyList()

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
