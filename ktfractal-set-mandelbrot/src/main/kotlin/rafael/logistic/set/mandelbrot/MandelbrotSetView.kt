package rafael.logistic.set.mandelbrot

import rafael.logistic.core.fx.Styles
import rafael.logistic.map.set.JuliaView
import tornadofx.App

class MandelbrotSetApp: App(MandelbrotSetView::class, Styles::class)

class MandelbrotSetView : JuliaView("Mandelbrot Set", "MandelbrotSet", MandelbrotSetGenerator()) {

    override fun getImageName(): String = "mandelbrot" +
            ".XMin=${xMinValueFactory.converter.toString(spnXMin.value)}" +
            ".XMax=${xMaxValueFactory.converter.toString(spnXMax.value)}" +
            ".YMin=${yMinValueFactory.converter.toString(spnYMin.value)}" +
            ".YMax=${yMaxValueFactory.converter.toString(spnYMax.value)}" +
            ".Iterations_Dot=${spnIterations.value}"

}
