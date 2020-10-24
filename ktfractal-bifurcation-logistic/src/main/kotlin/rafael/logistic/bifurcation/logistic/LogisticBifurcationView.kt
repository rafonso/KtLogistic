package rafael.logistic.bifurcation.logistic

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.configureMinMaxSpinners
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.map.bifurcation.BifurcationView
import rafael.logistic.map.bifurcation.RData
import tornadofx.App
import tornadofx.toProperty

class LogisticBifurcationApp : App(LogisticBifurcationView::class, Styles::class)

class LogisticBifurcationView : BifurcationView<LogisticBifurcationGenerator>(
    "Logistic Bifurcation",
    "LogisticBifurcation",
    LogisticBifurcationGenerator()
) {

    // @formatter:off
    private val spnX0: Spinner<Double> by fxid()
    private val deltaX0Property = 1.toProperty()
    private val x0ValueFactory = doubleSpinnerValueFactory(X_MIN, X_MAX, 0.5, 0.1)

    private val spnRMin: Spinner<Double> by fxid()
    private val rMinValueFactory = doubleSpinnerValueFactory(R_MIN, R_MAX, R_MIN, 0.1)

    private val spnRMax: Spinner<Double> by fxid()
    private val rMaxValueFactory = doubleSpinnerValueFactory(R_MIN, R_MAX, R_MAX, 0.1)

    private val deltaRLimitProperty = 1.toProperty()
    private val deltaRStepProperty = (0.1).toProperty()

    // @formatter:on

    override fun getParametersName() = "bifurcation" +
            ".X0=${x0ValueFactory.converter.toString(spnX0.value)}" +
            ".Iterations_R=${spnIterations.value}" +
            ".RMin=${rMinValueFactory.converter.toString(spnRMin.value)}" +
            ".RMax=${rMaxValueFactory.converter.toString(spnRMax.value)}"

    override fun initializeControls() {
        super.initializeControls()

        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)

        configureMinMaxSpinners(
            spnRMin, rMinValueFactory, spnRMax, rMaxValueFactory,
            deltaRLimitProperty, deltaRStepProperty, this::loadData
        )
    }

    override fun initializeCharts() {
        super.initializeCharts(X_MIN, X_MAX, spnX0, spnRMin, spnRMax)
    }

    override fun refreshData(
        generator: LogisticBifurcationGenerator, iterations: Int,
        stepsForR: Int,
        skip: Int
    ): List<RData> =
        generator.generate(
            spnX0.value, super.chart.xMin, super.chart.xMax,
            stepsForR, skip, iterations
        )

}
