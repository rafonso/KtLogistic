package rafael.logistic.bifurcation.ikeda

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.map.bifurcation.BifurcationView
import rafael.logistic.map.bifurcation.RData
import tornadofx.App
import tornadofx.toProperty

class IkedaBifurcationApp : App(IkedaBifurcationView::class, Styles::class)

class IkedaBifurcationView : BifurcationView<IkedaBifurcationGenerator>(
    "Ikeda Bifurcarion",
    "IkedaBifurcation",
    IkedaBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                           : Spinner<Double>   by fxid()
    private val deltaX0Property                 = 1.toProperty()
    private val x0ValueFactory                  = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnX1                           : Spinner<Double>   by fxid()
    private val deltaX1Property                 = 1.toProperty()
    private val x1ValueFactory                  = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnUMin                         : Spinner<Double>   by fxid()
    private val uMinValueFactory                = doubleSpinnerValueFactory(U_MIN, U_MAX, U_MIN, 0.1)

    private val spnUMax                         : Spinner<Double>   by fxid()
    private val uMaxValueFactory                = doubleSpinnerValueFactory(U_MIN, U_MAX, U_MAX, 0.1)

    private val deltaULimitProperty             = 1.toProperty()
    private val deltaUStepProperty              = (0.1).toProperty()

    // @formatter:on

    override fun getParametersName() = "lozi-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".X1=${x1ValueFactory.converter.toString(spnX1.value)}" +
            ".Iterations_U=${spnIterations.value}" +
            ".UMin=${uMinValueFactory.converter.toString(spnUMin.value)}" +
            ".UMax=${uMaxValueFactory.converter.toString(spnUMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnX1.configureActions(x1ValueFactory, deltaX1Property, this::loadData)

        configureMinMaxSpinners(
            spnUMin, uMinValueFactory, spnUMax, uMaxValueFactory,
            deltaULimitProperty, deltaUStepProperty, this::loadData
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(X_MIN, X_MAX, spnX0, spnUMin, spnUMax)
    }

    override fun refreshData(
        generator: IkedaBifurcationGenerator, iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX1.value,
            super.chart.xMin, super.chart.xMax,
            stepsForR,
            skip, iterations,
            spnX0.value
        )

}
