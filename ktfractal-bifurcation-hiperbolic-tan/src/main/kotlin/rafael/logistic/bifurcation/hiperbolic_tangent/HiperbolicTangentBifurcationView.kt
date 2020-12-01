package rafael.logistic.bifurcation.hiperbolic_tangent

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.decimalProperty
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.bifurcation.BifurcationView
import rafael.logistic.bifurcation.RData
import tornadofx.App

class HiperbolicTangentBifurcationApp : App(HiperbolicTangentBifurcationView::class, Styles::class)

class HiperbolicTangentBifurcationView : BifurcationView<HiperbolicTangentBifurcationGenerator>(
    "Hiperbolic Tangent Bifurcation",
    "HiperbolicTangentBifurcation",
    HiperbolicTangentBifurcationGenerator()
) {

    // @formatter:off

    private val spnX0               : Spinner<Double> by fxid()

    private val spnGMin             : Spinner<Double>   by fxid()
    private val gMinValueFactory    = doubleSpinnerValueFactory(G_MIN, G_MAX, G_MIN, 0.1)

    private val spnGMax             : Spinner<Double>   by fxid()
    private val gMaxValueFactory    = doubleSpinnerValueFactory(G_MIN, G_MAX, G_MAX, 0.1)

    private val deltaGLimitProperty = oneProperty()
    private val deltaGStepProperty  = decimalProperty()

    private val spnXMin             : Spinner<Double>   by fxid()
    private val xMinValueFactory    = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MIN, 0.1)

    private val spnXMax             : Spinner<Double>   by fxid()
    private val xMaxValueFactory    = doubleSpinnerValueFactory(X_MIN, X_MAX, X_MAX, 0.1)

    private val deltaXLimitProperty = oneProperty()
    private val deltaXStepProperty  = decimalProperty()

    override val spinnerComponents  = arrayOf(SpinnerConfigurations(spnX0, X_MIN, X_MAX, 1.0))

    // @formatter:on

    override fun getParametersName() = "hiperbolic-tangent-bifurcation" +
            ".X0=${spnX0.valueFactory.converter.toString(spnX0.value)}" +
            ".Iterations_G=${spnIterations.value}" +
            ".XMin=${xMinValueFactory.converter.toString(spnXMin.value)}" +
            ".XMax=${xMaxValueFactory.converter.toString(spnXMax.value)}" +
            ".GMin=${gMinValueFactory.converter.toString(spnGMin.value)}" +
            ".GMax=${gMaxValueFactory.converter.toString(spnGMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        configureXAxisSpinners(
            spnXMin,
            xMinValueFactory,
            spnXMax,
            xMaxValueFactory,
            deltaXLimitProperty,
            deltaXStepProperty
        )
        configureYAxisSpinners(
            spnGMin, gMinValueFactory, spnGMax, gMaxValueFactory,
            deltaGLimitProperty, deltaGStepProperty)
    }

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
