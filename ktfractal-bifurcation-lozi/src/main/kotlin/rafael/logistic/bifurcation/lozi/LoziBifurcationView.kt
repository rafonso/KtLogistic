package rafael.logistic.bifurcation.lozi

import javafx.scene.control.Spinner
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.*
import tornadofx.App

class LoziBifurcationApp : App(LoziBifurcationView::class, Styles::class)

class LoziBifurcationView : BifurcationView<LoziBifurcationGenerator>(
    "Lozi Bifurcation",
    "LoziBifurcation",
    LoziBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                   : Spinner<Double>   by fxid()

    private val spnX1                   : Spinner<Double>   by fxid()

    private val spnBeta                 : Spinner<Double>   by fxid()

    private val spnAlphaMin             : Spinner<Double>   by fxid()
    private val alphaMinValueFactory    = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, ALPHA_MIN, 0.1)

    private val spnAlphaMax             : Spinner<Double>   by fxid()
    private val alphaMaxValueFactory    = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, ALPHA_MAX, 0.1)

    private val spnXMin                 : Spinner<Double>   by fxid()
    private val xMinValueFactory        = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MIN, 0.1)

    private val spnXMax                 : Spinner<Double>   by fxid()
    private val xMaxValueFactory        = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MAX, 0.1)

    override val spinnerComponents      = arrayOf(
        SpinnerConfigurations(spnX0     , X_MIN     , X_MAX     , 0.0),
        SpinnerConfigurations(spnX1     , X_MIN     , X_MAX     , 0.0),
        SpinnerConfigurations(spnBeta   , BETA_MIN  , BETA_MAX  , 0.0),
    )

    // @formatter:on

    override fun getParametersName() = "lozi-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".X1=${spnX1.valueToString()}" +
            ".Beta=${spnBeta.valueToString()}" +
            ".Iterations_Alpha=${spnIterations.value}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".AlphaMin=${spnAlphaMin.valueToString()}" +
            ".AlphaMax=${spnAlphaMax.valueToString()}"

    override fun initializeControls() {
        super.initializeControls()

        configureXAxisSpinners(spnXMin, xMinValueFactory, spnXMax, xMaxValueFactory)
        configureYAxisSpinners(spnAlphaMin, alphaMinValueFactory, spnAlphaMax, alphaMaxValueFactory)
    }

    override fun initializeCharts() {
        super.initializeCharts(spnX0, spnAlphaMin, spnAlphaMax, spnXMin, spnXMax)
    }

    override fun refreshData(
        generator: LoziBifurcationGenerator, iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX1.value,
            super.chart.xMin, super.chart.xMax,
            stepsForR, skip, iterations,
            spnX0.value, spnBeta.value
        )

}
