package rafael.logistic.bifurcation.henon

import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import rafael.logistic.core.fx.DoubleSpinner
import rafael.logistic.core.fx.LimitsSpinnersConfiguration
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import tornadofx.App

class HenonBifurcationApp : App(HenonBifurcationView::class, Styles::class)

class HenonBifurcationView : BifurcationView<HenonBifurcationGenerator>(
    "Henon Bifurcation",
    "HenonBifurcation",
    HenonBifurcationGenerator()
) {

    // @formatter:off

    private val spnX0                   : DoubleSpinner   by fxid()
    private val spnX1                   : DoubleSpinner   by fxid()
    private val spnBeta                 : DoubleSpinner   by fxid()
    private val spnAlphaMin             : DoubleSpinner   by fxid()
    private val spnAlphaMax             : DoubleSpinner   by fxid()
    private val spnXMin                 : DoubleSpinner   by fxid()
    private val spnXMax                 : DoubleSpinner   by fxid()

    override val spinnerComponents      = arrayOf(
        SpinnerConfigurations(spnX0     , X_MIN     , X_MAX     , 0.0),
        SpinnerConfigurations(spnX1     , X_MIN     , X_MAX     , 0.0),
        SpinnerConfigurations(spnBeta   , BETA_MIN  , BETA_MAX  , 0.0),
    )
    override val spnX0Axis              = spnX0
    override val xAxisConfiguration     = LimitsSpinnersConfiguration(spnAlphaMin   , spnAlphaMax   , ALPHA_MIN , ALPHA_MAX )
    override val yAxisConfiguration     = LimitsSpinnersConfiguration(spnXMin       , spnXMax       , X_MIN     , X_MAX     )

    // @formatter:on

    override fun getParametersName(iterations: Int) = "henon-bifurcation" +
            ".X0=${spnX0.valueToString()}" +
            ".X1=${spnX1.valueToString()}" +
            ".Beta=${spnBeta.valueToString()}" +
            ".Iterations_Alpha=${iterations}" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".AlphaMin=${spnAlphaMin.valueToString()}" +
            ".AlphaMax=${spnAlphaMax.valueToString()}"


    override fun refreshData(
        generator: HenonBifurcationGenerator,
        iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX1.value,
            super.chart.xMin, super.chart.xMax,
            stepsForR,
            skip, iterations,
            spnX0.value, spnBeta.value
        )

}
