package rafael.logistic.maps.duffing

import javafx.geometry.Point2D
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.view.configureActions
import rafael.logistic.view.view.ViewBi
import tornadofx.*

class DuffingView : ViewBi<DuffingGenerator>("Duffing", "Duffing", DuffingGenerator()) {

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

    override fun refreshData(generator: DuffingGenerator, iterations: Int): List<Point2D> =
            generator.generate(Point2D(x0Property.value, y0Property.value), spnA.value, spnB.value, iterations)

    override fun initializeControlsBi() {
        spnA.configureActions(aValueFactory, deltaAProperty, this::loadData)
        spnB.configureActions(bValueFactory, deltaBProperty, this::loadData)
    }

}
