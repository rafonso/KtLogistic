package rafael.logistic.maps.mandelbrot

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.geometry.Point2D
import rafael.logistic.view.view.ViewBi
import rafael.logistic.view.configureActions
import tornadofx.*

class MandelbrotView : ViewBi<MandelbrotGenerator>("Mandelbrot", "Mandelbrot", MandelbrotGenerator()) {

    // @formatter:off
    private val spnCX            :   Spinner<Double>   by fxid()
    private val deltaCXProperty  =   1.toProperty()
    private val cXValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-2.0, 2.0, -0.5, maxDelta)

    private val spnCY            :   Spinner<Double>   by fxid()
    private val deltaCYProperty  =   1.toProperty()
    private val cYValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-2.0, 2.0, -0.5, maxDelta)
    // @formatter:on

    override fun refreshData(generator: MandelbrotGenerator, iterations: Int): List<Point2D> =
            generator.generate(Point2D(x0Property.value, y0Property.value), spnCX.value, spnCY.value, iterations)

    override fun initializeControlsBi() {
        spnCX.configureActions(cXValueFactory, deltaCXProperty, this::loadData)
        spnCY.configureActions(cYValueFactory, deltaCYProperty, this::loadData)
    }

}
