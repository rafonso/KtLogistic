package rafael.logistic.bifurcation.tent

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.decimalProperty
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.map.bifurcation.BifurcationView
import rafael.logistic.map.bifurcation.RData
import tornadofx.App

class TentBifurcationApp : App(TentBifurcationView::class, Styles::class)

class TentBifurcationView : BifurcationView<TentBifurcationGenerator>(
    "Tent Bifurcation",
    "TentBifurcation",
    TentBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                   : Spinner<Double>   by fxid()
    private val deltaX0Property         = oneProperty()
    private val x0ValueFactory          = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.5, 0.1)

    private val spnMiMin                : Spinner<Double>   by fxid()
    private val miMinValueFactory       = doubleSpinnerValueFactory(MI_MIN, MI_MAX, MI_MIN, 0.1)

    private val spnMiMax                : Spinner<Double>   by fxid()
    private val miMaxValueFactory       = doubleSpinnerValueFactory(MI_MIN, MI_MAX, MI_MAX, 0.1)

    private val deltaMiLimitProperty    = oneProperty()
    private val deltaMiStepProperty     = decimalProperty()

    private val spnXMin                 : Spinner<Double>   by fxid()
    private val xMinValueFactory        = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MIN, 0.1)

    private val spnXMax                 : Spinner<Double>   by fxid()
    private val xMaxValueFactory        = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MAX, 0.1)

    private val deltaXLimitProperty     = oneProperty()
    private val deltaXStepProperty      = decimalProperty()

    // @formatter:on

    override fun getParametersName() = "tent-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Iterations_R=${spnIterations.value}" +
            ".XMin=${xMinValueFactory.converter.toString(spnXMin.value)}" +
            ".XMax=${xMaxValueFactory.converter.toString(spnXMax.value)}" +
            ".RMin=${miMinValueFactory.converter.toString(spnMiMin.value)}" +
            ".RMax=${miMaxValueFactory.converter.toString(spnMiMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        spnX0.configureSpinner(x0ValueFactory, deltaX0Property)

        configureXAxisSpinners(
            spnXMin,
            xMinValueFactory,
            spnXMax,
            xMaxValueFactory,
            deltaXLimitProperty,
            deltaXStepProperty
        )
        configureYAxisSpinners(
            spnMiMin, miMinValueFactory, spnMiMax, miMaxValueFactory,
            deltaMiLimitProperty, deltaMiStepProperty
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(spnX0, spnMiMin, spnMiMax, spnXMin, spnXMax)
    }

    override fun refreshData(
        generator: TentBifurcationGenerator, iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX0.value, super.chart.xMin, super.chart.xMax,
            stepsForR, skip, iterations
        )

}
