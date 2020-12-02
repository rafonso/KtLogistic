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
    private     val spnRMax             : Spinner<Double>   by fxid()
    private     val spnXMin             : Spinner<Double>   by fxid()
    private     val spnXMax             : Spinner<Double>   by fxid()

    override    val spinnerComponents   = arrayOf(SpinnerConfigurations(spnX0, X_MIN, X_MAX, 0.5))
    override    val spnX0Axis           = spnX0
    override    val xAxisConfiguration  = LimitsSpinnersConfiguration(spnRMin, spnRMax, R_MIN, R_MAX)
    override    val yAxisConfiguration  = LimitsSpinnersConfiguration(spnXMin, spnXMax, X_MIN, X_MAX)

    // @formatter:on

    override fun getParametersName() = "bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".Iterations_R=${spnIterations.value}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".RMin=${spnRMin.valueToString()}" +
            ".RMax=${spnRMax.valueToString()}"

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
