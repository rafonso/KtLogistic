package rafael.logistic.map.gaussian

import rafael.logistic.core.fx.DoubleSpinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.map.fx.view.ViewDouble
import tornadofx.App

class GaussianMapApp: App(GaussianMapView::class, Styles::class)

class GaussianMapView : ViewDouble<GaussianMapGenerator, GaussianMapChart>("Gaussian Map", "GaussianMap", GaussianMapGenerator()) {

    // @formatter:off

    private val spnAlpha            :   DoubleSpinner   by fxid()
    private val spnBeta             :   DoubleSpinner   by fxid()

    override val spinnerComponents  = arrayOf(
        SpinnerConfigurations(spnAlpha, 0.0, 10.0, 1.0),
        SpinnerConfigurations(spnBeta , -1.0, 0.0, 0.0),
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
