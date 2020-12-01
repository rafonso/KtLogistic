package rafael.logistic.map.duffing

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App

class DuffingMapApp: App(DuffingMapView::class, Styles::class)

class DuffingMapView : ViewBi<DuffingMapGenerator>("Duffing Map", "DuffingMap", DuffingMapGenerator()) {

    override val iniX0Spinner: Double
        get() = 1.0

    // @formatter:off
    private val spnA                :   Spinner<Double>   by fxid()

    private val spnB                :   Spinner<Double>   by fxid()

    override val spinnerComponents  = arrayOf(
        SpinnerConfigurations(spnA, 2.0, 3.0, 2.75, 2),
        SpinnerConfigurations(spnB, 0.0, 0.5, 0.15, 2),
    )

    // @formatter:on

    override fun refreshData(generator: DuffingMapGenerator, iterations: Int): List<BiDouble> =
        generator.generate(BiDouble(x0Property.value, y0Property.value), spnA.value, spnB.value, iterations)

    override fun getImageName1(): String = "duffing.Alpha=${spnA.valueToString()}.Beta=${spnB.valueToString()}"

}
