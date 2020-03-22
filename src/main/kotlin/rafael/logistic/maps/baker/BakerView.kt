package rafael.logistic.maps.baker

import javafx.geometry.Point2D
import rafael.logistic.core.view.ViewBi

class BakerView : ViewBi<BakerGenerator>("Baker", "Baker", BakerGenerator()) {

    override fun refreshData(generator: BakerGenerator, iterations: Int): List<Point2D> =
            generator.generate(Point2D(x0Property.value, y0Property.value), iterations)

}
