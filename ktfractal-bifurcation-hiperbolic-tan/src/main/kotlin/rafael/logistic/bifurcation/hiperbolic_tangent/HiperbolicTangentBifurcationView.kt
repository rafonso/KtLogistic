package rafael.logistic.bifurcation.hiperbolic_tangent

import javafx.scene.control.Spinner
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.*
import tornadofx.App

class HiperbolicTangentBifurcationApp : App(HiperbolicTangentBifurcationView::class, Styles::class)

class HiperbolicTangentBifurcationView : BifurcationView<HiperbolicTangentBifurcationGenerator>(
    "Hiperbolic Tangent Bifurcation",
    "HiperbolicTangentBifurcation",
    HiperbolicTangentBifurcationGenerator()
) {

    // @formatter:off

    private     val spnX0               : Spinner<Double>   by fxid()
    private     val spnGMin             : Spinner<Double>   by fxid()
    private     val spnGMax             : Spinner<Double>   by fxid()
    private     val spnXMin             : Spinner<Double>   by fxid()
    private     val spnXMax             : Spinner<Double>   by fxid()

    override    val spinnerComponents  = arrayOf(SpinnerConfigurations(spnX0, X_MIN, X_MAX, 1.0))
    override    val xAxisConfiguration = LimitsSpinnersConfiguration(spnGMin, spnGMax, G_MIN, G_MAX)
    override    val yAxisConfiguration = LimitsSpinnersConfiguration(spnXMin, spnXMax, X_MIN, X_MAX)

    // @formatter:on

    override fun getParametersName() = "hiperbolic-tangent-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".Iterations_G=${spnIterations.value}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".GMin=${spnGMin.valueToString()}" +
            ".GMax=${spnGMax.valueToString()}"

    override fun initializeCharts() {
        super.initializeCharts(spnX0, spnGMin, spnGMax, spnXMin, spnXMax)
    }

    override fun refreshData(
        generator: HiperbolicTangentBifurcationGenerator, iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX0.value, super.chart.xMin, super.chart.xMax,
            stepsForR,
            skip, iterations
        )

}
