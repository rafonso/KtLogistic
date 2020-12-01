package rafael.logistic.map.gaussian

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.map.fx.view.ViewDouble
import tornadofx.App

class GaussianMapApp: App(GaussianMapView::class, Styles::class)

class GaussianMapView : ViewDouble<GaussianMapGenerator, GaussianMapChart>("Gaussian Map", "GaussianMap", GaussianMapGenerator()) {

    // @formatter:off
    private val spnAlpha            :   Spinner<Double>   by fxid()
    private val alphaValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 1.0, maxDelta)

    private val spnBeta             :   Spinner<Double>   by fxid()
    private val betaValueFactory    =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 0.0, 0.0, maxDelta)

    override val spinnerComponents  = arrayOf(
        SpinnerComponents(spnAlpha, alphaValueFactory),
        SpinnerComponents(spnBeta , betaValueFactory ),
    )

    // @formatter:on

    override fun refreshData(generator: GaussianMapGenerator, iterations: Int): List<Double> =
            generator.generate(x0Property.value, spnAlpha.value, spnBeta.value, iterations)

    override fun initializeAdditional() {
        chart.alphaProperty.bind(spnAlpha.valueProperty())
        chart.betaProperty.bind(spnBeta.valueProperty())
    }

    override fun getImageName1(): String = "gaussian.Alpha=${spnAlpha.valueToString()}.Beta=${spnBeta.valueToString()}"

}
