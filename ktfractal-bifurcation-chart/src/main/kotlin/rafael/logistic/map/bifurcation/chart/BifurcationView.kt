package rafael.logistic.map.bifurcation.chart

import javafx.scene.chart.NumberAxis
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.view.ViewBase
import rafael.logistic.core.generation.GenerationStatusChronometerListener
import tornadofx.asObservable
import tornadofx.onChange
import tornadofx.runLater
import tornadofx.toProperty

class BifurcationView : ViewBase<RData, LogisticBifurcationGenerator, BifurcationChart>(
    "Bifurcation Chart",
    "BifurcationChart",
    LogisticBifurcationGenerator()
) {

    // @formatter:off
    private     val spnX0               :   Spinner<Double>     by  fxid()
    private     val deltaX0Property     =   1.toProperty()
    private     val x0ValueFactory      =   doubleSpinnerValueFactory(X_MIN, X_MAX, 0.5, 0.1)

    private     val spnSkip             :   Spinner<Int>        by  fxid()
    private     val skipValueFactory    =   SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0, 1)

    private     val spnPixelsSeparation :   Spinner<Int>        by  fxid()
    private     val pixelsSeparationValueFactory    =   SpinnerValueFactory.ListSpinnerValueFactory(listOf(0, 1, 2, 4, 10, 50, 100).asObservable())

    private     val spnRMin             :   Spinner<Double>     by  fxid()
    private     val rMinValueFactory    =   doubleSpinnerValueFactory(R_MIN, R_MAX, R_MIN, 0.1)

    private     val spnRMax             :   Spinner<Double>     by  fxid()
    private     val rMaxValueFactory    =   doubleSpinnerValueFactory(R_MIN, R_MAX, R_MAX, 0.1)

    private     val deltaRLimitProperty =   1.toProperty()
    private     val deltaRStepProperty  =   (0.1).toProperty()

    private     val lblPosMouse         :   MouseRealPosNode    by  fxid()

    private     val lblStatus           :   Label               by  fxid()
    // @formatter:on

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)

        spnSkip.configureActions(skipValueFactory, this::loadData)

        pixelsSeparationValueFactory.value = 10
        spnPixelsSeparation.configureActions(pixelsSeparationValueFactory, this::loadData)

        configureMinMaxSpinners(
            spnRMin, rMinValueFactory, spnRMax, rMaxValueFactory,
            deltaRLimitProperty, deltaRStepProperty, this::loadData
        )
    }

    override fun initializeCharts() {
        val chartParent = chart.parent as Region
        chart.prefWidthProperty().bind(chartParent.widthProperty())
        chart.prefHeightProperty().bind(chartParent.heightProperty())

        chart.x0Property.bind(spnX0.valueProperty())

        chart.xAxis.widthProperty().onChange { loadData() }
        chart.xMinProperty.bind(spnRMin.valueProperty())
        chart.xMaxProperty.bind(spnRMax.valueProperty())
    }

    override fun refreshData(generator: LogisticBifurcationGenerator, iterations: Int): List<RData> {
        val rAxis = (super.chart.xAxis as NumberAxis)

        return if (rAxis.widthProperty().value > 0)
            return generator.generate(
                spnX0.value, rAxis.lowerBound, rAxis.upperBound,
                rAxis.widthProperty().value.toInt() / (spnPixelsSeparation.value + 1), spnSkip.value, iterations
            )
        else
            emptyList()
    }

    override fun initializeAdditional() {
        lblPosMouse.bind(chart)
        GenerationStatusChronometerListener.bind(super.generationStatusProperty())
        super.generationStatusProperty().onChange {
            runLater {
                lblStatus.text = it?.toString()
            }
        }
    }

    override fun getImageName(): String = "bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Iterations_R=${spnIterations.value}" +
            ".RMin=${rMinValueFactory.converter.toString(spnRMin.value)}" +
            ".RMax=${rMaxValueFactory.converter.toString(spnRMax.value)}" +
            (if (spnSkip.value > 0) ".Skip=${spnSkip.value}pct" else "") +
            (if (spnPixelsSeparation.value > 0) ".PxnSep=${spnPixelsSeparation.value}" else "")

}
