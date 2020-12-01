package rafael.logistic.map.tinkerbell

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App

class TinkerbellMapApp : App(TinkerbellMapView::class, Styles::class)

class TinkerbellMapView : ViewBi<TinkerbellMapGenerator>("Tinkerbell Map", "TinkerbellMap", TinkerbellMapGenerator()) {

    override val iniX0Spinner: Double
        get() = -0.72

    override val maxX0Spinner: Double
        get() = 1.5

    override val minX0Spinner: Double
        get() = -maxX0Spinner

    override val iniY0Spinner: Double
        get() = -0.64

    override val maxY0Spinner: Double
        get() = 1.0

    override val minY0Spinner: Double
        get() = -maxY0Spinner

    // @formatter:off

    private     val spnA                :   Spinner<Double>   by fxid()

    private     val spnB                :   Spinner<Double>   by fxid()

    private     val spnC                :   Spinner<Double>   by fxid()

    private     val spnD                :   Spinner<Double>   by fxid()

    override    val spinnerComponents   =   arrayOf(
        SpinnerConfigurations(spnA,  0.0, 1.0,  0.9),
        SpinnerConfigurations(spnB, -1.0, 1.0, -0.6013, 4),
        SpinnerConfigurations(spnC, -4.0, 4.0,  2.0),
        SpinnerConfigurations(spnD,  0.0, 1.0,  0.5),
    )

    // @formatter:on

    override fun refreshData(generator: TinkerbellMapGenerator, iterations: Int): List<BiDouble> =
        generator.generate(
            BiDouble(x0Property.value, y0Property.value),
            spnA.value,
            spnB.value,
            spnC.value,
            spnD.value,
            iterations
        )

    override fun getImageName1(): String = "Tinkerbell" +
            ".a=${spnA.valueToString()}.b=${spnB.valueToString()}.c=${spnC.valueToString()}.d=${spnD.valueToString()}"

}
