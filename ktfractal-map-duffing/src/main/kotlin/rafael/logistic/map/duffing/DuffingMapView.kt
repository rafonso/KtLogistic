package rafael.logistic.map.duffing

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.fx.view.ViewBi
import rafael.logistic.core.generation.BiDouble
import tornadofx.toProperty

class DuffingMapView : ViewBi<DuffingMapGenerator>("Duffing Map", "DuffingMap", DuffingMapGenerator()) {

    override val iniX0Spinner: Double
        get() = 1.0

    // @formatter:off
    private val spnA            :   Spinner<Double>   by fxid()
    private val deltaAProperty  =   2.toProperty()
    private val aValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(2.0, 3.0, 2.75, maxDelta)

    private val spnB            :   Spinner<Double>   by fxid()
    private val deltaBProperty  =   2.toProperty()
    private val bValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 0.5, 0.15, maxDelta)
    // @formatter:on

    override fun refreshData(generator: DuffingMapGenerator, iterations: Int): List<BiDouble> =
        generator.generate(BiDouble(x0Property.value, y0Property.value), spnA.value, spnB.value, iterations)

    override fun initializeControlsBi() {
        spnA.configureActions(aValueFactory, deltaAProperty, this::loadData)
        spnB.configureActions(bValueFactory, deltaBProperty, this::loadData)
    }

    override fun getImageName1(): String = "duffing.Alpha=${spnA.valueToString()}.Beta=${spnB.valueToString()}"

}
