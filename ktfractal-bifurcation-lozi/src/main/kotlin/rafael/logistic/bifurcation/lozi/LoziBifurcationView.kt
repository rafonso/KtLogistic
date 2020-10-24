package rafael.logistic.bifurcation.lozi

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.map.bifurcation.BifurcationView
import rafael.logistic.map.bifurcation.RData
import tornadofx.App
import tornadofx.toProperty

class LoziBifurcationApp : App(LoziBifurcationView::class, Styles::class)

class LoziBifurcationView : BifurcationView<LoziBifurcationGenerator>(
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

    private val spnAlphaMin                     : Spinner<Double>   by fxid()
    private val alphaMinValueFactory            = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, ALPHA_MIN, 0.1)

    private val spnAlphaMax                     : Spinner<Double>   by fxid()
    private val alphaMaxValueFactory            = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, ALPHA_MAX, 0.1)

    private val deltaAlphaLimitProperty         = 1.toProperty()
    private val deltaAlphaStepProperty          = (0.1).toProperty()

    // @formatter:on

    override fun getParametersName() = "lozi-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".X1=${x1ValueFactory.converter.toString(spnX1.value)}" +
            ".Beta=${betaValueFactory.converter.toString(spnBeta.value)}" +
            ".Iterations_Alpha=${spnIterations.value}" +
            ".AlphaMin=${alphaMinValueFactory.converter.toString(spnAlphaMin.value)}" +
            ".AlphaMax=${alphaMaxValueFactory.converter.toString(spnAlphaMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnX1.configureActions(x1ValueFactory, deltaX1Property, this::loadData)
        spnBeta.configureActions(betaValueFactory, deltaBetaProperty, this::loadData)

        configureMinMaxSpinners(
            spnAlphaMin, alphaMinValueFactory, spnAlphaMax, alphaMaxValueFactory,
            deltaAlphaLimitProperty, deltaAlphaStepProperty, this::loadData
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(X_MIN, X_MAX, spnX0, spnAlphaMin, spnAlphaMax)
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
