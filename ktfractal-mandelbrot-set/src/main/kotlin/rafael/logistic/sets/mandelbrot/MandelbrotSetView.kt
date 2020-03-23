package rafael.logistic.sets.mandelbrot

import rafael.logistic.maps.sets.JuliaView

class MandelbrotSetView : JuliaView("Mandelbrot Set", "MandelbrotSet",
    MandelbrotSetGenerator()
)
