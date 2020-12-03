package rafael.logistic.map.standard

import rafael.logistic.core.fx.DoubleSpinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App

class StandardMapApp : App(StandardMapView::class, Styles::class)

class StandardMapView : ViewBi<StandardMapGenerator>("Standard Map", "StandardMap", StandardMapGenerator()) {

    // @formatter:off
    private     val spnK                :   DoubleSpinner by fxid()

    override    val spinnerComponents   =   arrayOf(SpinnerConfigurations(spnK, MIN_K, MAX_K, 1.0))

    // @formatter:on

    override fun refreshData(generator: StandardMapGenerator, iterations: Int): List<BiDouble> =
        generator.generate(BiDouble(x0Property.value, y0Property.value), spnK.value, iterations)

    override fun getImageName1(): String = "K=${spnK.valueToString()}"

}
