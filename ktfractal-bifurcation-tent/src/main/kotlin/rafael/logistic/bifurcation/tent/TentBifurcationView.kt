package rafael.logistic.bifurcation.tent

import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.DoubleSpinner
import rafael.logistic.core.fx.LimitsSpinnersConfiguration
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import tornadofx.App

class TentBifurcationApp : App(TentBifurcationView::class, Styles::class)

class TentBifurcationView : BifurcationView<TentBifurcationGenerator>(
    "Tent Bifurcation",
    "TentBifurcation",
    TentBifurcationGenerator()
) {

    // @formatter:off

    private     val spnX0               : DoubleSpinner   by fxid()
    private     val spnMiMin            : DoubleSpinner   by fxid()
    private     val spnMiMax            : DoubleSpinner   by fxid()
    private     val spnXMin             : DoubleSpinner   by fxid()
    private     val spnXMax             : DoubleSpinner   by fxid()

    override    val spinnerComponents   = arrayOf(SpinnerConfigurations(spnX0, X_MIN, X_MAX, 0.5))
    override    val spnX0Axis           = spnX0
    override    val xAxisConfiguration  = LimitsSpinnersConfiguration(spnMiMin  , spnMiMax  , MI_MIN    , MI_MAX  )
    override    val yAxisConfiguration  = LimitsSpinnersConfiguration(spnXMin   , spnXMax   , X_MIN     , X_MAX   )

    // @formatter:on

    override fun getParametersName(iterations: Int) = "tent-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".Iterations_R=${iterations}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".RMin=${spnMiMin.valueToString()}" +
            ".RMax=${spnMiMax.valueToString()}"

    override fun refreshData(
        generator: TentBifurcationGenerator, iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX0.value, super.chart.xMin, super.chart.xMax,
            stepsForR, skip, iterations
        )

}
