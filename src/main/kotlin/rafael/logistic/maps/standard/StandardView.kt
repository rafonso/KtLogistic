package rafael.logistic.maps.standard

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.configureActions
import rafael.logistic.view.view.ViewBi
import tornadofx.*

class StandardView : ViewBi<StandardGenerator>("Standard", "Standard", StandardGenerator()) {

    // @formatter:off
    private val spnK            :   Spinner<Double> by fxid()
    private val deltaKProperty  =   1.toProperty()
    private val kValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 5.0, 1.0, maxDelta)
    // @formatter:on

    override fun initializeControlsBi() {
        spnK.configureActions(kValueFactory, deltaKProperty, this::loadData)
    }

    override fun refreshData(generator: StandardGenerator, iterations: Int): List<BiPoint> =
            generator.generate(BiPoint(x0Property.value, y0Property.value), spnK.value, iterations)

}
