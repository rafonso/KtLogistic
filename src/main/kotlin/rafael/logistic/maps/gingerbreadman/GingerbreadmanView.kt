package rafael.logistic.maps.gingerbreadman

import javafx.geometry.Point2D
import rafael.logistic.view.view.ViewBi

class GingerbreadmanView : ViewBi<GingerbreadmanGenerator>("Ginger bread man", "Gingerbreadman", GingerbreadmanGenerator()) {

    override val maxDelta: Double
        get() = 1.0

    override val maxX0Spinner: Double
        get() = 10.0

    override val minX0Spinner: Double
        get() = -maxX0Spinner

    override val maxY0Spinner: Double
        get() = maxX0Spinner

    override val minY0Spinner: Double
        get() = -maxY0Spinner


    override fun refreshData(generator: GingerbreadmanGenerator, iterations: Int): List<Point2D> =
            generator.generate(Point2D(x0Property.value, y0Property.value), iterations)

}
