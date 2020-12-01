package rafael.logistic.bifurcation.ikeda

import javafx.scene.control.Spinner
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.*
import tornadofx.App

class IkedaBifurcationApp : App(IkedaBifurcationView::class, Styles::class)

class IkedaBifurcationView : BifurcationView<IkedaBifurcationGenerator>(
    "Ikeda Bifurcation",
    "IkedaBifurcation",
    IkedaBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0                           : Spinner<Double>   by fxid()

    private val spnX1                           : Spinner<Double>   by fxid()

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

        override val spinnerComponents              = arrayOf(
            SpinnerConfigurations(spnX0     , X_MIN, X_MAX, 0.0),
            SpinnerConfigurations(spnX1     , X_MIN, X_MAX, 0.0),
        )

    // @formatter:on

    override fun getParametersName() = "lozi-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".X1=${spnX1.valueToString()}" +
            ".Iterations_U=${spnIterations.value}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".UMin=${spnUMin.valueToString()}" +
            ".UMax=${spnUMax.valueToString()}"

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
