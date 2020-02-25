package rafael.logistic.maps.mandelbrot

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.view.ViewBi
import rafael.logistic.view.configureActions
import tornadofx.*

class MandelbrotView : ViewBi<MandelbrotGenerator>("Mandelbrot", "Mandelbrot", MandelbrotGenerator()) {

    override val iniX0Spinner: Double
        get() = 1.0

    // @formatter:off
    private val spnRe            :   Spinner<Double>   by fxid()
    private val deltaReProperty  =   1.toProperty()
    private val reValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-2.0, 1.0, 0.5, maxDelta)

    private val spnIm            :   Spinner<Double>   by fxid()
    private val deltaImProperty  =   1.toProperty()
    private val imValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 1.0, 0.5, maxDelta)
    // @formatter:on

    override fun refreshData(generator: MandelbrotGenerator, iterations: Int): List<BiPoint> =
            generator.generate(BiPoint(x0Property.value, y0Property.value), spnRe.value, spnIm.value, iterations)

    override fun initializeControlsBi() {
        spnRe.configureActions(reValueFactory, deltaReProperty, this::loadData)
        spnIm.configureActions(imValueFactory, deltaImProperty, this::loadData)
    }

}
