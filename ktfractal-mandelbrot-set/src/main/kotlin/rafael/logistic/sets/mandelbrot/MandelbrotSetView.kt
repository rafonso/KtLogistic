package rafael.logistic.sets.mandelbrot

import rafael.logistic.maps.sets.JuliaView

class MandelbrotSetView : JuliaView("Mandelbrot Set", "MandelbrotSet", MandelbrotSetGenerator()) {

    override fun getImageName(): String = "mandelbrot" +
            ".XMin=${xMinValueFactory.converter.toString(spnXMin.value)}" +
            ".XMax=${xMaxValueFactory.converter.toString(spnXMax.value)}" +
            ".YMin=${yMinValueFactory.converter.toString(spnYMin.value)}" +
            ".YMax=${yMaxValueFactory.converter.toString(spnYMax.value)}" +
            ".Iterations_Dot=${spnIterations.value}"

}
