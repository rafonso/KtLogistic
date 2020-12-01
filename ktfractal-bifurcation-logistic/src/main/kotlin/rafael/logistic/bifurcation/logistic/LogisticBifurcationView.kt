package rafael.logistic.bifurcation.logistic

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.decimalProperty
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import tornadofx.App

class LogisticBifurcationApp : App(LogisticBifurcationView::class, Styles::class)

class LogisticBifurcationView : BifurcationView<LogisticBifurcationGenerator>(
    "Logistic Bifurcation",
    "LogisticBifurcation",
    LogisticBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0               : Spinner<Double> by fxid()
    private val x0ValueFactory      = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.5, 0.1)

    private val spnRMin             : Spinner<Double> by fxid()
    private val rMinValueFactory    = doubleSpinnerValueFactory(R_MIN, R_MAX, R_MIN, 0.1)

    private val spnRMax             : Spinner<Double>   by fxid()
    private val rMaxValueFactory    = doubleSpinnerValueFactory(R_MIN, R_MAX, R_MAX, 0.1)

    private val deltaRLimitProperty = oneProperty()
    private val deltaRStepProperty  = decimalProperty()

    private val spnXMin             : Spinner<Double>   by fxid()
    private val xMinValueFactory    = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MIN, 0.1)

    private val spnXMax             : Spinner<Double>   by fxid()
    private val xMaxValueFactory    = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MAX, 0.1)

    private val deltaXLimitProperty = oneProperty()
    private val deltaXStepProperty  = decimalProperty()

    override val spinnerComponents  = arrayOf(SpinnerComponents(spnX0, x0ValueFactory))

    // @formatter:on

    override fun getParametersName() = "bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Iterations_R=${spnIterations.value}" +
            ".XMin=${xMinValueFactory.converter.toString(spnXMin.value)}" +
            ".XMax=${xMaxValueFactory.converter.toString(spnXMax.value)}" +
            ".RMin=${rMinValueFactory.converter.toString(spnRMin.value)}" +
            ".RMax=${rMaxValueFactory.converter.toString(spnRMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        configureXAxisSpinners(
            spnXMin, xMinValueFactory, spnXMax, xMaxValueFactory,
            deltaXLimitProperty, deltaXStepProperty
        )
        configureYAxisSpinners(
            spnRMin, rMinValueFactory, spnRMax, rMaxValueFactory,
            deltaRLimitProperty, deltaRStepProperty
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(spnX0, spnRMin, spnRMax, spnXMin, spnXMax)
    }

    override fun refreshData(
        generator: LogisticBifurcationGenerator, iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX0.value, super.chart.xMin, super.chart.xMax,
            stepsForR, skip, iterations
        )

}
