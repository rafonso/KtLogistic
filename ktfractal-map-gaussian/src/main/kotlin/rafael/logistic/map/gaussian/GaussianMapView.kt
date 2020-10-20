package rafael.logistic.map.gaussian

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.view.ViewDouble
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.valueToString
import tornadofx.*

class GaussianMapApp: App(GaussianMapView::class, Styles::class)

class GaussianMapView : ViewDouble<GaussianMapGenerator, GaussianMapChart>("Gaussian Map", "GaussianMap", GaussianMapGenerator()) {

    // @formatter:off
    private val spnAlpha            :   Spinner<Double>   by fxid()
    private val deltaAlphaProperty  =   1.toProperty()
    private val alphaValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 1.0, maxDelta)

    private val spnBeta             :   Spinner<Double>   by fxid()
    private val deltaBetaProperty   =   1.toProperty()
    private val betaValueFactory    =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 0.0, 0.0, maxDelta)
    // @formatter:on

    override fun refreshData(generator: GaussianMapGenerator, iterations: Int): List<Double> =
            generator.generate(x0Property.value, spnAlpha.value, spnBeta.value, iterations)

    override fun initializeControlsDouble() {
        spnAlpha.configureActions(alphaValueFactory, deltaAlphaProperty, this::loadData)
        spnBeta.configureActions(betaValueFactory, deltaBetaProperty, this::loadData)
    }

    override fun initializeAdditional() {
        chart.alphaProperty.bind(spnAlpha.valueProperty())
        chart.betaProperty.bind(spnBeta.valueProperty())
    }

    override fun getImageName1(): String = "gaussian.Alpha=${spnAlpha.valueToString()}.Beta=${spnBeta.valueToString()}"

}
