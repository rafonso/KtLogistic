package rafael.logistic.bifurcation.gaussian

import javafx.scene.control.Spinner
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.*
import tornadofx.App

class GaussianBifurcationApp : App(GaussianBifurcationView::class, Styles::class)

class GaussianBifurcationView : BifurcationView<GaussianBifurcationGenerator>(
    "Gaussian Bifurcation",
    "GaussianBifurcation",
    GaussianBifurcationGenerator()
) {

    // @formatter:off

    private val spnX0                   : Spinner<Double>   by fxid()
    private val spnAlpha                : Spinner<Double>   by fxid()
    private val spnBetaMin              : Spinner<Double>   by fxid()
    private val spnBetaMax              : Spinner<Double>   by fxid()
    private val spnXMin                 : Spinner<Double>   by fxid()
    private val spnXMax                 : Spinner<Double>   by fxid()

    override val spinnerComponents      = arrayOf(
        SpinnerConfigurations(spnX0     , X_MIN     , X_MAX     , 0.0),
        SpinnerConfigurations(spnAlpha  , ALPHA_MIN , ALPHA_MAX , 5.0)
    )
    override val spnX0Axis              = spnX0
    override val xAxisConfiguration     = LimitsSpinnersConfiguration(spnBetaMin, spnBetaMax, BETA_MIN  , BETA_MAX  )
    override val yAxisConfiguration     = LimitsSpinnersConfiguration(spnXMin   , spnXMax   , X_MIN     , X_MAX     )

    // @formatter:on

    override fun getParametersName() = "gaussian-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".Alpha=${spnAlpha.valueToString()}" +
            ".Iterations_Beta=${spnIterations.value}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".BetaMin=${spnBetaMin.valueToString()}" +
            ".BetaMax=${spnBetaMax.valueToString()}"

    override fun refreshData(
        generator: GaussianBifurcationGenerator,
        iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX0.value,
            super.chart.xMin, super.chart.xMax,
            stepsForR,
            skip, iterations,
            spnAlpha.value
        )

}
