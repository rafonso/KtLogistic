package rafael.logistic.map.lozi

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.map.fx.view.ViewBi
import tornadofx.App

class LoziMapApp: App(LoziMapView::class, Styles::class)

class LoziMapView : ViewBi<LoziMapGenerator>("Lozi Map", "LoziMap", LoziMapGenerator()) {

    // @formatter:off
    private     val spnA                :   Spinner<Double>   by fxid()
    private     val aValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 2.0, 1.4, maxDelta)

    private     val spnB                :   Spinner<Double>   by fxid()
    private     val bValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.3, maxDelta)

    override    val spinnerComponents   = arrayOf(
        SpinnerComponents(spnA, aValueFactory),
        SpinnerComponents(spnB, bValueFactory),
    )

    // @formatter:on

    override fun refreshData(generator: LoziMapGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), spnA.value, spnB.value, iterations)

    override fun getImageName1(): String = "lozi.Alpha=${spnA.valueToString()}.Beta=${spnB.valueToString()}"

}
