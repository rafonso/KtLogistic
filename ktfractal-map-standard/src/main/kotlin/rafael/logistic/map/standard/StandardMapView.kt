package rafael.logistic.map.standard

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App

class StandardMapApp : App(StandardMapView::class, Styles::class)

class StandardMapView : ViewBi<StandardMapGenerator>("Standard Map", "StandardMap", StandardMapGenerator()) {

    // @formatter:off
    private     val spnK                :   Spinner<Double> by fxid()
    private     val kValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(MIN_K, MAX_K, 1.0, maxDelta)

    override    val spinnerComponents   =   arrayOf(SpinnerComponents(spnK, kValueFactory))

    // @formatter:on

    override fun refreshData(generator: StandardMapGenerator, iterations: Int): List<BiDouble> =
        generator.generate(BiDouble(x0Property.value, y0Property.value), spnK.value, iterations)

    override fun getImageName1(): String = "K=${spnK.valueToString()}"

}
