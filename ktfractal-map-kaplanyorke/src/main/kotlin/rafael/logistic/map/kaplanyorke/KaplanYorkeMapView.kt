package rafael.logistic.map.kaplanyorke

import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.spinners.DoubleSpinner
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.*

class KaplanYorkeMapApp: App(KaplanYorkeMapView::class, Styles::class)

class KaplanYorkeMapView : ViewBi<KaplanYorkeMapGenerator>("Kaplan-Yorke Map", "KaplanYorkeMap", KaplanYorkeMapGenerator()) {

    override val iniX0Spinner: Double
        get() = 0.5

    // @formatter:off

    private     val spnA                :   DoubleSpinner   by fxid()

    override    val spinnerComponents   =   arrayOf(SpinnerConfigurations(spnA, 0.0, 1.0, 0.1))

    // @formatter:on

    override fun refreshData(generator: KaplanYorkeMapGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), spnA.value, iterations)

    override fun getImageName1(): String = "kaplan-yorke.Alpha=${spnA.valueToString()}"

}
