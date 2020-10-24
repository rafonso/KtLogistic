package rafael.logistic.bifurcation.gaussian

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.map.bifurcation.BifurcationView
import rafael.logistic.map.bifurcation.RData
import tornadofx.App
import tornadofx.toProperty

class GaussianBifurcationApp : App(GaussianBifurcationView::class, Styles::class)

class GaussianBifurcationView : BifurcationView<GaussianBifurcationGenerator>(
    "Gaussian Bifurcation",
    "GaussianBifurcation",
    GaussianBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                   : Spinner<Double>   by fxid()
    private val deltaX0Property         = 1.toProperty()
    private val x0ValueFactory          = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnAlpha                : Spinner<Double>   by fxid()
    private val deltaAlphaProperty      = 1.toProperty()
    private val alphaValueFactory       = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, 5.0, 0.1)

    private val spnBetaMin              : Spinner<Double>   by fxid()
    private val betaMinValueFactory     = doubleSpinnerValueFactory(BETA_MIN, BETA_MAX, BETA_MIN, 0.1)

    private val spnBetaMax              : Spinner<Double>   by fxid()
    private val betaMaxValueFactory     = doubleSpinnerValueFactory(BETA_MIN, BETA_MAX, BETA_MAX, 0.1)

    private val deltaBetaLimitProperty  = 1.toProperty()
    private val deltaBetaStepProperty   = (0.1).toProperty()

    // @formatter:on

    override fun getParametersName() = "gaussian-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Alpha=${alphaValueFactory.converter.toString(spnAlpha.value)}" +
            ".Iterations_Beta=${spnIterations.value}" +
            ".BetaMin=${betaMinValueFactory.converter.toString(spnBetaMin.value)}" +
            ".BetaMax=${betaMaxValueFactory.converter.toString(spnBetaMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnAlpha.configureActions(alphaValueFactory, deltaAlphaProperty, this::loadData)

        configureMinMaxSpinners(
            spnBetaMin, betaMinValueFactory, spnBetaMax, betaMaxValueFactory,
            deltaBetaLimitProperty, deltaBetaStepProperty, this::loadData
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(X_MIN, X_MAX, spnX0, spnBetaMin, spnBetaMax)
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
