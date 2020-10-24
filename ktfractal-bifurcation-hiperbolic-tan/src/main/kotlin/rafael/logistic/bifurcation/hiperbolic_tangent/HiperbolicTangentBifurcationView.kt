package rafael.logistic.bifurcation.hiperbolic_tangent

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.map.bifurcation.BifurcationView
import rafael.logistic.map.bifurcation.RData
import tornadofx.App
import tornadofx.toProperty

class HiperbolicTangentBifurcationApp : App(HiperbolicTangentBifurcationView::class, Styles::class)

class HiperbolicTangentBifurcationView : BifurcationView<HiperbolicTangentBifurcationGenerator>(
    "Hiperbolic Tangent Bifurcation",
    "HiperbolicTangentBifurcation",
    HiperbolicTangentBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0: Spinner<Double> by fxid()
    private val deltaX0Property = 1.toProperty()
    private val x0ValueFactory = doubleSpinnerValueFactory(X_MIN, X_MAX, 1.0, 0.1)

    private val spnGMin: Spinner<Double> by fxid()
    private val gMinValueFactory = doubleSpinnerValueFactory(G_MIN, G_MAX, G_MIN, 0.1)

    private val spnGMax: Spinner<Double> by fxid()
    private val gMaxValueFactory = doubleSpinnerValueFactory(G_MIN, G_MAX, G_MAX, 0.1)

    private val deltaGLimitProperty = 1.toProperty()
    private val deltaGStepProperty = (0.1).toProperty()

    // @formatter:on

    override fun getParametersName() = "hiperbolic-tangent-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Iterations_G=${spnIterations.value}" +
            ".GMin=${gMinValueFactory.converter.toString(spnGMin.value)}" +
            ".GMax=${gMaxValueFactory.converter.toString(spnGMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)

        configureMinMaxSpinners(
            spnGMin, gMinValueFactory, spnGMax, gMaxValueFactory,
            deltaGLimitProperty, deltaGStepProperty, this::loadData
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(X_MIN, X_MAX, spnX0, spnGMin, spnGMax)
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
