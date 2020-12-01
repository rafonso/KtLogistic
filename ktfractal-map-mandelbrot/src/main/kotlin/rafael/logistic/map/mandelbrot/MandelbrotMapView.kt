package rafael.logistic.map.mandelbrot

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App

class MandelbrotMapApp: App(MandelbrotMapView::class, Styles::class)

class MandelbrotMapView : ViewBi<MandelbrotMapGenerator>("Mandelbrot Map", "MandelbrotMap", MandelbrotMapGenerator()) {

    // @formatter:off
    private     val spnCX               :   Spinner<Double>   by fxid()
    private     val cXValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(-2.0, 2.0, -0.5, maxDelta)

    private     val spnCY               :   Spinner<Double>   by fxid()
    private     val cYValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(-2.0, 2.0, -0.5, maxDelta)

    override    val spinnerComponents   =   arrayOf(
        SpinnerComponents(spnCX, cXValueFactory),
        SpinnerComponents(spnCY, cYValueFactory),
    )

    // @formatter:on

    override fun refreshData(generator: MandelbrotMapGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), spnCX.value, spnCY.value, iterations)

    override fun getImageName1(): String = "mandelbrot.CX=${spnCX.valueToString()}.CY=${spnCY.valueToString()}"

}
