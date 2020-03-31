package rafael.logistic.maps.standard

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.fx.view.ViewBi
import rafael.logistic.core.generation.BiDouble
import tornadofx.toProperty

class StandardView : ViewBi<StandardGenerator>("Standard", "Standard", StandardGenerator()) {

    // @formatter:off
    private val spnK            :   Spinner<Double> by fxid()
    private val deltaKProperty  =   1.toProperty()
    private val kValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 5.0, 1.0, maxDelta)
    // @formatter:on

    override fun initializeControlsBi() {
        spnK.configureActions(kValueFactory, deltaKProperty, this::loadData)
    }

    override fun refreshData(generator: StandardGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), spnK.value, iterations)

    override fun getImageName1(): String = "standard.K=${spnK.valueToString()}"

}
