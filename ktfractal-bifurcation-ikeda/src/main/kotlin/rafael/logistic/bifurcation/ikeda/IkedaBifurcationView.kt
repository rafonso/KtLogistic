package rafael.logistic.bifurcation.ikeda

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.decimalProperty
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.map.bifurcation.BifurcationView
import rafael.logistic.map.bifurcation.RData
import tornadofx.App

class IkedaBifurcationApp : App(IkedaBifurcationView::class, Styles::class)

class IkedaBifurcationView : BifurcationView<IkedaBifurcationGenerator>(
    "Ikeda Bifurcation",
    "IkedaBifurcation",
    IkedaBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                           : Spinner<Double>   by fxid()
    private val deltaX0Property                 = oneProperty()
    private val x0ValueFactory                  = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnX1                           : Spinner<Double>   by fxid()
    private val deltaX1Property                 = oneProperty()
    private val x1ValueFactory                  = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.0, 0.1)

    private val spnUMin                         : Spinner<Double>   by fxid()
    private val uMinValueFactory                = doubleSpinnerValueFactory(U_MIN, U_MAX, U_MIN, 0.1)

    private val spnUMax                         : Spinner<Double>   by fxid()
    private val uMaxValueFactory                = doubleSpinnerValueFactory(U_MIN, U_MAX, U_MAX, 0.1)

    private val deltaULimitProperty             = oneProperty()
    private val deltaUStepProperty              = decimalProperty()

    private val spnXMin                         : Spinner<Double>   by fxid()
    private val xMinValueFactory                = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MIN, 0.1)

    private val spnXMax                         : Spinner<Double>   by fxid()
    private val xMaxValueFactory                = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MAX, 0.1)

    private val deltaXLimitProperty             = oneProperty()
    private val deltaXStepProperty              = decimalProperty()

    // @formatter:on

    override fun getParametersName() = "lozi-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".X1=${x1ValueFactory.converter.toString(spnX1.value)}" +
            ".Iterations_U=${spnIterations.value}" +
            ".XMin=${xMinValueFactory.converter.toString(spnXMin.value)}" +
            ".XMax=${xMaxValueFactory.converter.toString(spnXMax.value)}" +
            ".UMin=${uMinValueFactory.converter.toString(spnUMin.value)}" +
            ".UMax=${uMaxValueFactory.converter.toString(spnUMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        spnX0.configureSpinner(x0ValueFactory, deltaX0Property)
        spnX1.configureSpinner(x1ValueFactory, deltaX1Property)

        configureXAxisSpinners(
            spnXMin,
            xMinValueFactory,
            spnXMax,
            xMaxValueFactory,
            deltaXLimitProperty,
            deltaXStepProperty
        )
        configureYAxisSpinners(
            spnUMin, uMinValueFactory, spnUMax, uMaxValueFactory,
            deltaULimitProperty, deltaUStepProperty
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(spnX0, spnUMin, spnUMax, spnXMin, spnXMax)
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
