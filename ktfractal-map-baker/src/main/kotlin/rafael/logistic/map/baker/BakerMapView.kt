package rafael.logistic.map.baker

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.fx.view.ViewBi

class BakerMapView : ViewBi<BakerMapGenerator>("Baker Map", "BakerMap", BakerMapGenerator()) {

    override fun refreshData(generator: BakerMapGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), iterations)

    override fun getImageName1(): String = "baker"

}
