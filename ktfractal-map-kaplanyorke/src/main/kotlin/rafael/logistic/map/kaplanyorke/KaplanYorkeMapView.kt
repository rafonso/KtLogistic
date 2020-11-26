package rafael.logistic.map.kaplanyorke

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.map.fx.view.ViewBi
import rafael.logistic.core.generation.BiDouble
import tornadofx.App

class KaplanYorkeMapApp: App(KaplanYorkeMapView::class, Styles::class)

class KaplanYorkeMapView : ViewBi<KaplanYorkeMapGenerator>("Kaplan-Yorke Map", "KaplanYorkeMap", KaplanYorkeMapGenerator()) {

    override val iniX0Spinner: Double
        get() = 0.5

    // @formatter:off
    private val spnA            :   Spinner<Double>   by fxid()
    private val deltaAProperty  =   SimpleIntegerProperty(this, "deltaAlpha"    , 1     )
    private val aValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.1, maxDelta)

    // @formatter:on

    override fun refreshData(generator: KaplanYorkeMapGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), spnA.value, iterations)

    override fun initializeControlsBi() {
        spnA.configureSpinner(aValueFactory, deltaAProperty)
    }

    override fun getImageName1(): String = "kaplan-yorke.Alpha=${spnA.valueToString()}"

}
