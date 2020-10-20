package rafael.logistic.map.mandelbrot

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.fx.view.ViewBi
import rafael.logistic.core.generation.BiDouble
import tornadofx.toProperty

class MandelbrotMapView : ViewBi<MandelbrotMapGenerator>("Mandelbrot Map", "MandelbrotMap", MandelbrotMapGenerator()) {

    // @formatter:off
    private val spnCX            :   Spinner<Double>   by fxid()
    private val deltaCXProperty  =   1.toProperty()
    private val cXValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-2.0, 2.0, -0.5, maxDelta)

    private val spnCY            :   Spinner<Double>   by fxid()
    private val deltaCYProperty  =   1.toProperty()
    private val cYValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-2.0, 2.0, -0.5, maxDelta)
    // @formatter:on

    override fun refreshData(generator: MandelbrotMapGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), spnCX.value, spnCY.value, iterations)

    override fun initializeControlsBi() {
        spnCX.configureActions(cXValueFactory, deltaCXProperty, this::loadData)
        spnCY.configureActions(cYValueFactory, deltaCYProperty, this::loadData)
    }

    override fun getImageName1(): String = "mandelbrot.CX=${spnCX.valueToString()}.CY=${spnCY.valueToString()}"

}
