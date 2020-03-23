package rafael.logistic.maps.standard

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.geometry.Point2D
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.view.ViewBi
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

    override fun refreshData(generator: StandardGenerator, iterations: Int): List<Point2D> =
            generator.generate(Point2D(x0Property.value, y0Property.value), spnK.value, iterations)

}
