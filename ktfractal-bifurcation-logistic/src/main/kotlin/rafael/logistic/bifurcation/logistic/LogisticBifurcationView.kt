package rafael.logistic.bifurcation.logistic

import javafx.scene.control.Spinner
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.*
import tornadofx.App

class LogisticBifurcationApp : App(LogisticBifurcationView::class, Styles::class)

class LogisticBifurcationView : BifurcationView<LogisticBifurcationGenerator>(
    "Logistic Bifurcation",
    "LogisticBifurcation",
    LogisticBifurcationGenerator()
) {

    // @formatter:off

    private     val spnX0               : Spinner<Double>   by fxid()

    private     val spnRMin             : Spinner<Double>   by fxid()
    private     val rMinValueFactory    = doubleSpinnerValueFactory(R_MIN, R_MAX, R_MIN, 0.1)

    private     val spnRMax             : Spinner<Double>   by fxid()
    private     val rMaxValueFactory    = doubleSpinnerValueFactory(R_MIN, R_MAX, R_MAX, 0.1)

    private     val spnXMin             : Spinner<Double>   by fxid()
    private     val xMinValueFactory    = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MIN, 0.1)

    private     val spnXMax             : Spinner<Double>   by fxid()
    private     val xMaxValueFactory    = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MAX, 0.1)

    override    val spinnerComponents   = arrayOf(SpinnerConfigurations(spnX0, X_MIN, X_MAX, 0.5))

    // @formatter:on

    override fun getParametersName() = "bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".Iterations_R=${spnIterations.value}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".RMin=${spnRMin.valueToString()}" +
            ".RMax=${spnRMax.valueToString()}"

    override fun initializeControls() {
        super.initializeControls()

        configureXAxisSpinners(spnXMin, xMinValueFactory, spnXMax, xMaxValueFactory)
        configureYAxisSpinners(spnRMin, rMinValueFactory, spnRMax, rMaxValueFactory)
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
