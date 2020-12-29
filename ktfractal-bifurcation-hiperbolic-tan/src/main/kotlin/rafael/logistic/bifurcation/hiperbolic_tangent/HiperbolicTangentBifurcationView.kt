package rafael.logistic.bifurcation.hiperbolic_tangent

import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.spinners.DoubleSpinner
import rafael.logistic.core.fx.spinners.LimitsSpinnersConfiguration
import tornadofx.*

class HiperbolicTangentBifurcationApp : App(HiperbolicTangentBifurcationView::class, Styles::class)

class HiperbolicTangentBifurcationView : BifurcationView<HiperbolicTangentBifurcationGenerator>(
    "Hiperbolic Tangent Bifurcation",
    "HiperbolicTangentBifurcation",
    HiperbolicTangentBifurcationGenerator()
) {

    // @formatter:off

    private     val spnX0               : DoubleSpinner   by fxid()
    private     val spnGMin             : DoubleSpinner   by fxid()
    private     val spnGMax             : DoubleSpinner   by fxid()
    private     val spnXMin             : DoubleSpinner   by fxid()
    private     val spnXMax             : DoubleSpinner   by fxid()

    override    val spinnerComponents   = arrayOf(SpinnerConfigurations(spnX0, X_MIN, X_MAX, 1.0))
    override    val spnX0Axis           = spnX0
    override    val xAxisConfiguration  = LimitsSpinnersConfiguration(spnGMin, spnGMax, G_MIN, G_MAX)
    override    val yAxisConfiguration  = LimitsSpinnersConfiguration(spnXMin, spnXMax, X_MIN, X_MAX)

    // @formatter:on

    override fun getParametersName(iterations: Int) = "hiperbolic-tangent-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".Iterations_G=${iterations}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".GMin=${spnGMin.valueToString()}" +
            ".GMax=${spnGMax.valueToString()}"

    override fun refreshData(
        generator: HiperbolicTangentBifurcationGenerator, iterations: Int,
        stepsForR: Int,
        firstIteration: Int
    ): List<RData> =
        generator.generate(
            spnX0.value, super.chart.xMin, super.chart.xMax,
            stepsForR,
            firstIteration, iterations
        )

}
