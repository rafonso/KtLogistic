package rafael.logistic.maps.baker

import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.fx.view.ViewBi

class BakerView : ViewBi<BakerGenerator>("Baker", "Baker", BakerGenerator()) {

    override fun refreshData(generator: BakerGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), iterations)

    override fun getImageName1(): String = "baker"

}
