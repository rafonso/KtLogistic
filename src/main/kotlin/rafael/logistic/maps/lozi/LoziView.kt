package rafael.logistic.maps.lozi

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.geometry.Point2D
import rafael.logistic.core.*
import rafael.logistic.core.view.ViewBi

class LoziView : ViewBi<LoziGenerator>("Lozi", "Lozi", LoziGenerator()) {

    // @formatter:off
    private  val spnA           :   Spinner<Double>   by fxid()
    private val deltaAProperty  =   SimpleIntegerProperty(this, "deltaAlpha"    , 1     )
    private val aValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 2.0, 1.4, maxDelta)

    private  val spnB           :   Spinner<Double>   by fxid()
    private val deltaBProperty  =   SimpleIntegerProperty(this, "deltaBeta"    , 1     )
    private val bValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.3, maxDelta)
    // @formatter:on

    override fun refreshData(generator: LoziGenerator, iterations: Int): List<Point2D> =
            generator.generate(Point2D(x0Property.value, y0Property.value), spnA.value, spnB.value, iterations)

    override fun initializeControlsBi() {
        spnA.configureActions(aValueFactory, deltaAProperty, this::loadData)
        spnB.configureActions(bValueFactory, deltaBProperty, this::loadData)
    }

}
