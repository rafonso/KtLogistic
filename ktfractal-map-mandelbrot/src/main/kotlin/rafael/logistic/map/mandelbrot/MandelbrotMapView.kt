package rafael.logistic.map.mandelbrot

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App

class MandelbrotMapApp: App(MandelbrotMapView::class, Styles::class)

class MandelbrotMapView : ViewBi<MandelbrotMapGenerator>("Mandelbrot Map", "MandelbrotMap", MandelbrotMapGenerator()) {

    // @formatter:off
    private     val spnCX               :   Spinner<Double>   by fxid()

    private     val spnCY               :   Spinner<Double>   by fxid()

    override    val spinnerComponents   =   arrayOf(
        SpinnerConfigurations(spnCX, -2.0, 2.0, -0.5),
        SpinnerConfigurations(spnCY, -2.0, 2.0, -0.5),
    )

    // @formatter:on

    override fun refreshData(generator: MandelbrotMapGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), spnCX.value, spnCY.value, iterations)

    override fun getImageName1(): String = "mandelbrot.CX=${spnCX.valueToString()}.CY=${spnCY.valueToString()}"

}
