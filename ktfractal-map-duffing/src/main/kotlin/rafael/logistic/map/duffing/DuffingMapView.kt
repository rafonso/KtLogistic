package rafael.logistic.map.duffing

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.map.fx.view.ViewBi
import rafael.logistic.core.generation.BiDouble
import tornadofx.App
import tornadofx.toProperty

class DuffingMapApp: App(DuffingMapView::class, Styles::class)

class DuffingMapView : ViewBi<DuffingMapGenerator>("Duffing Map", "DuffingMap", DuffingMapGenerator()) {

    override val iniX0Spinner: Double
        get() = 1.0

    // @formatter:off
    private val spnA                :   Spinner<Double>   by fxid()
    private val aValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(2.0, 3.0, 2.75, maxDelta)

    private val spnB                :   Spinner<Double>   by fxid()
    private val bValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 0.5, 0.15, maxDelta)

    override val spinnerComponents  = arrayOf(
        SpinnerComponents(spnA, aValueFactory, 2.toProperty()),
        SpinnerComponents(spnB, bValueFactory, 2.toProperty()),
    )

    // @formatter:on

    override fun refreshData(generator: DuffingMapGenerator, iterations: Int): List<BiDouble> =
        generator.generate(BiDouble(x0Property.value, y0Property.value), spnA.value, spnB.value, iterations)

    override fun initializeControlsBi() {
    }

    override fun getImageName1(): String = "duffing.Alpha=${spnA.valueToString()}.Beta=${spnB.valueToString()}"

}
