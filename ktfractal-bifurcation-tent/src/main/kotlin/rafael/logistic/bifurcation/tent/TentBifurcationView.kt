package rafael.logistic.bifurcation.tent

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.map.bifurcation.BifurcationView
import rafael.logistic.map.bifurcation.RData
import tornadofx.App
import tornadofx.toProperty

class TentBifurcationApp : App(TentBifurcationView::class, Styles::class)

class TentBifurcationView : BifurcationView<TentBifurcationGenerator>(
    "Tent Bifurcation",
    "TentBifurcation",
    TentBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0: Spinner<Double> by fxid()
    private val deltaX0Property = 1.toProperty()
    private val x0ValueFactory = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.5, 0.1)

    private val spnMiMin: Spinner<Double> by fxid()
    private val miMinValueFactory = doubleSpinnerValueFactory(MI_MIN, MI_MAX, MI_MIN, 0.1)

    private val spnMiMax: Spinner<Double> by fxid()
    private val miMaxValueFactory = doubleSpinnerValueFactory(MI_MIN, MI_MAX, MI_MAX, 0.1)

    private val deltaMiLimitProperty = 1.toProperty()
    private val deltaMiStepProperty = (0.1).toProperty()

    // @formatter:on

    override fun getParametersName() = "tent-bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Iterations_R=${spnIterations.value}" +
            ".RMin=${miMinValueFactory.converter.toString(spnMiMin.value)}" +
            ".RMax=${miMaxValueFactory.converter.toString(spnMiMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)

        configureMinMaxSpinners(
            spnMiMin, miMinValueFactory, spnMiMax, miMaxValueFactory,
            deltaMiLimitProperty, deltaMiStepProperty, this::loadData
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(X_MIN, X_MAX, spnX0, spnMiMin, spnMiMax)
    }

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
