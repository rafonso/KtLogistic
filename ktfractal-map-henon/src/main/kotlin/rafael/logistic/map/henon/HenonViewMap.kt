package rafael.logistic.map.henon

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App

class HenonMapApp: App(HenonViewMap::class, Styles::class)

class HenonViewMap : ViewBi<HenonMapGenerator>("Henon Map", "HenonMap", HenonMapGenerator()) {

    // @formatter:off
    private val spnA            :   Spinner<Double>   by fxid()
    private val aValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 2.0, 1.4, maxDelta)

    private val spnB            :   Spinner<Double>   by fxid()
    private val bValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.3, maxDelta)

    override val spinnerComponents  = arrayOf(
        SpinnerComponents(spnA, aValueFactory),
        SpinnerComponents(spnB, bValueFactory),
    )

    // @formatter:on

    override fun refreshData(generator: HenonMapGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), spnA.value, spnB.value, iterations)

    override fun getImageName1(): String = "henon.A=${spnA.valueToString()}.B=${spnB.valueToString()}"

}
