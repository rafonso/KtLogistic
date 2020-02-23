package rafael.logistic.maps.baker

import rafael.logistic.generator.BiPoint
import rafael.logistic.view.view.ViewBi

class BakerView : ViewBi<BakerGenerator>("Baker", "Baker", BakerGenerator()) {

    override fun refreshData(generator: BakerGenerator, iterations: Int): List<BiPoint> =
            generator.generate(BiPoint(x0Property.value, y0Property.value), iterations)

}
