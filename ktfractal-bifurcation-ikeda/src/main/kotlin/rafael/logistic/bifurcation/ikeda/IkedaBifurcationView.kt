package rafael.logistic.bifurcation.ikeda

import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.DoubleSpinner
import rafael.logistic.core.fx.LimitsSpinnersConfiguration
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import tornadofx.App

class IkedaBifurcationApp : App(IkedaBifurcationView::class, Styles::class)

class IkedaBifurcationView : BifurcationView<IkedaBifurcationGenerator>(
    "Ikeda Bifurcation",
    "IkedaBifurcation",
    IkedaBifurcationGenerator()
) {

    // @formatter:off

    private     val spnX0               : DoubleSpinner   by fxid()
    private     val spnX1               : DoubleSpinner   by fxid()
    private     val spnUMin             : DoubleSpinner   by fxid()
    private     val spnUMax             : DoubleSpinner   by fxid()
    private     val spnXMin             : DoubleSpinner   by fxid()
    private     val spnXMax             : DoubleSpinner   by fxid()

    override    val spinnerComponents   = arrayOf(
            SpinnerConfigurations(spnX0     , X_MIN, X_MAX, 0.0),
            SpinnerConfigurations(spnX1     , X_MIN, X_MAX, 0.0),
        )
    override    val spnX0Axis           = spnX0
    override    val xAxisConfiguration  = LimitsSpinnersConfiguration(spnUMin, spnUMax, U_MIN, U_MAX)
    override    val yAxisConfiguration  = LimitsSpinnersConfiguration(spnXMin, spnXMax, X_MIN, X_MAX)

    // @formatter:on

    override fun getParametersName() = "lozi-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".X1=${spnX1.valueToString()}" +
            ".Iterations_U=${spnIterations.value}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".UMin=${spnUMin.valueToString()}" +
            ".UMax=${spnUMax.valueToString()}"

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
