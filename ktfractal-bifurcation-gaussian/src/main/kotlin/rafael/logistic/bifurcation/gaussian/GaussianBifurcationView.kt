package rafael.logistic.bifurcation.gaussian

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.decimalProperty
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import tornadofx.App

class GaussianBifurcationApp : App(GaussianBifurcationView::class, Styles::class)

class GaussianBifurcationView : BifurcationView<GaussianBifurcationGenerator>(
    "Gaussian Bifurcation",
    "GaussianBifurcation",
    GaussianBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                   : Spinner<Double>   by fxid()
    private val x0ValueFactory          = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnAlpha                : Spinner<Double>   by fxid()
    private val alphaValueFactory       = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, 5.0, 0.1)

    private val spnBetaMin              : Spinner<Double>   by fxid()
    private val betaMinValueFactory     = doubleSpinnerValueFactory(BETA_MIN, BETA_MAX, BETA_MIN, 0.1)

    private val spnBetaMax              : Spinner<Double>   by fxid()
    private val betaMaxValueFactory     = doubleSpinnerValueFactory(BETA_MIN, BETA_MAX, BETA_MAX, 0.1)

    private val deltaBetaLimitProperty  = oneProperty()
    private val deltaBetaStepProperty   = decimalProperty()

    private val spnXMin                 : Spinner<Double>   by fxid()
    private val xMinValueFactory        = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MIN, 0.1)

    private val spnXMax                 : Spinner<Double>   by fxid()
    private val xMaxValueFactory        = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MAX, 0.1)

    private val deltaXLimitProperty     = oneProperty()
    private val deltaXStepProperty      = decimalProperty()

    override val spinnerComponents      = arrayOf(
        SpinnerComponents(spnX0     , x0ValueFactory   ),
        SpinnerComponents(spnAlpha  , alphaValueFactory)
    )

    // @formatter:on

    override fun getParametersName() = "gaussian-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Alpha=${alphaValueFactory.converter.toString(spnAlpha.value)}" +
            ".Iterations_Beta=${spnIterations.value}" +
            ".XMin=${xMinValueFactory.converter.toString(spnXMin.value)}" +
            ".XMax=${xMaxValueFactory.converter.toString(spnXMax.value)}" +
            ".BetaMin=${betaMinValueFactory.converter.toString(spnBetaMin.value)}" +
            ".BetaMax=${betaMaxValueFactory.converter.toString(spnBetaMax.value)}"

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
            spnBetaMin, betaMinValueFactory, spnBetaMax, betaMaxValueFactory,
            deltaBetaLimitProperty, deltaBetaStepProperty
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(spnX0, spnBetaMin, spnBetaMax, spnXMin, spnXMax)
    }

    override fun refreshData(
        generator: GaussianBifurcationGenerator,
        iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX0.value,
            super.chart.xMin, super.chart.xMax,
            stepsForR,
            skip, iterations,
            spnAlpha.value
        )

}
