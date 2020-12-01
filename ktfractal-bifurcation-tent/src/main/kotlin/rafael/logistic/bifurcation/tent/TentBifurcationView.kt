package rafael.logistic.bifurcation.tent

import javafx.scene.control.Spinner
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.*
import tornadofx.App

class TentBifurcationApp : App(TentBifurcationView::class, Styles::class)

class TentBifurcationView : BifurcationView<TentBifurcationGenerator>(
    "Tent Bifurcation",
    "TentBifurcation",
    TentBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                   : Spinner<Double>   by fxid()

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

    override val spinnerComponents      = arrayOf(SpinnerConfigurations(spnX0, X_MIN, X_MAX, 0.5))

    // @formatter:on

    override fun getParametersName() = "tent-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".Iterations_R=${spnIterations.value}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".RMin=${spnMiMin.valueToString()}" +
            ".RMax=${spnMiMax.valueToString()}"

    override fun initializeControls() {
        super.initializeControls()

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
