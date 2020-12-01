package rafael.logistic.map.baker

import rafael.logistic.core.fx.Styles
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App

class BakerMapApp: App(BakerMapView::class, Styles::class)

class BakerMapView : ViewBi<BakerMapGenerator>("Baker Map", "BakerMap", BakerMapGenerator()) {

    override val spinnerComponents = emptyArray<SpinnerConfigurations>()

    override fun refreshData(generator: BakerMapGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), iterations)

    override fun getImageName1(): String = "baker"

}
