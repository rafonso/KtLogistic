package rafael.logistic.bifurcation.tent

import javafx.scene.control.Spinner
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.*
import tornadofx.App

class TentBifurcationApp : App(TentBifurcationView::class, Styles::class)

class TentBifurcationView : BifurcationView<TentBifurcationGenerator>(
    "Tent Bifurcation",
    "TentBifurcation",
    TentBifurcationGenerator()
) {

    // @formatter:off

    private     val spnX0               : Spinner<Double>   by fxid()
    private     val spnMiMin            : Spinner<Double>   by fxid()
    private     val spnMiMax            : Spinner<Double>   by fxid()
    private     val spnXMin             : Spinner<Double>   by fxid()
    private     val spnXMax             : Spinner<Double>   by fxid()

    override    val spinnerComponents   = arrayOf(SpinnerConfigurations(spnX0, X_MIN, X_MAX, 0.5))
    override    val spnX0Axis           = spnX0
    override    val xAxisConfiguration  = LimitsSpinnersConfiguration(spnMiMin  , spnMiMax  , MI_MIN    , MI_MAX  )
    override    val yAxisConfiguration  = LimitsSpinnersConfiguration(spnXMin   , spnXMax   , X_MIN     , X_MAX   )

    // @formatter:on

    override fun getParametersName() = "tent-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".Iterations_R=${spnIterations.value}" +
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
