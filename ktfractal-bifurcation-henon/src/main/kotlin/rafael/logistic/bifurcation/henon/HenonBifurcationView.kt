package rafael.logistic.bifurcation.henon

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.decimalProperty
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.map.bifurcation.BifurcationView
import rafael.logistic.map.bifurcation.RData
import tornadofx.App

class HenonBifurcationApp : App(HenonBifurcationView::class, Styles::class)

class HenonBifurcationView : BifurcationView<HenonBifurcationGenerator>(
    "Henon Bifurcation",
    "HenonBifurcation",
    HenonBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                   : Spinner<Double>   by fxid()
    private val deltaX0Property         = oneProperty()
    private val x0ValueFactory          = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnX1                   : Spinner<Double>   by fxid()
    private val deltaX1Property         = oneProperty()
    private val x1ValueFactory          = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnBeta                 : Spinner<Double>   by fxid()
    private val deltaBetaProperty       = oneProperty()
    private val betaValueFactory        = doubleSpinnerValueFactory(BETA_MIN, BETA_MAX, 0.0, 0.1)

    private val spnAlphaMin             : Spinner<Double>   by fxid()
    private val alphaMinValueFactory    = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, ALPHA_MIN, 0.1)

    private val spnAlphaMax             : Spinner<Double>   by fxid()
    private val alphaMaxValueFactory    = doubleSpinnerValueFactory(ALPHA_MIN, ALPHA_MAX, ALPHA_MAX, 0.1)

    private val deltaAlphaLimitProperty = oneProperty()
    private val deltaAlphaStepProperty  = decimalProperty()

    private val spnXMin                 : Spinner<Double>   by fxid()
    private val xMinValueFactory        = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MIN, 0.1)

    private val spnXMax                 : Spinner<Double>   by fxid()
    private val xMaxValueFactory        = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MAX, 0.1)

    private val deltaXLimitProperty     = oneProperty()
    private val deltaXStepProperty      = decimalProperty()

    // @formatter:on

    override fun getParametersName() = "henon-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".X1=${x1ValueFactory.converter.toString(spnX1.value)}" +
            ".Beta=${betaValueFactory.converter.toString(spnBeta.value)}" +
            ".Iterations_Alpha=${spnIterations.value}" +
            ".XMin=${xMinValueFactory.converter.toString(spnXMin.value)}" +
            ".XMax=${xMaxValueFactory.converter.toString(spnXMax.value)}" +
            ".AlphaMin=${alphaMinValueFactory.converter.toString(spnAlphaMin.value)}" +
            ".AlphaMax=${alphaMaxValueFactory.converter.toString(spnAlphaMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        spnX0.configureSpinner(x0ValueFactory, deltaX0Property)
        spnX1.configureSpinner(x1ValueFactory, deltaX1Property)
        spnBeta.configureSpinner(betaValueFactory, deltaBetaProperty)

        configureXAxisSpinners(
            spnXMin,
            xMinValueFactory,
            spnXMax,
            xMaxValueFactory,
            deltaXLimitProperty,
            deltaXStepProperty
        )
        configureYAxisSpinners(
            spnAlphaMin, alphaMinValueFactory, spnAlphaMax, alphaMaxValueFactory,
            deltaAlphaLimitProperty, deltaAlphaStepProperty
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(spnX0, spnAlphaMin, spnAlphaMax, spnXMin, spnXMax)
    }

    override fun refreshData(
        generator: HenonBifurcationGenerator,
        iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX1.value,
            super.chart.xMin, super.chart.xMax,
            stepsForR,
            skip, iterations,
            spnX0.value, spnBeta.value
        )

}
