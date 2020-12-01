package rafael.logistic.set.mandelbrot

import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.set.SetView
import tornadofx.App

class MandelbrotSetApp: App(MandelbrotSetView::class, Styles::class)

class MandelbrotSetView : SetView("Mandelbrot Set", "MandelbrotSet", MandelbrotSetGenerator()) {

    override val spinnerComponents = emptyArray<SpinnerConfigurations>()

    override fun getImageName(): String = "mandelbrot" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".YMin=${spnYMin.valueToString()}" +
            ".YMax=${spnYMax.valueToString()}" +
            ".Iterations_Dot=${spnIterations.value}"

}
