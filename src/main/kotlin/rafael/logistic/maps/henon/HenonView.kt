package rafael.logistic.maps.henon

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.*
import rafael.logistic.view.view.ViewBi

class HenonView : ViewBi<HenonGenerator>("Henon", "Henon", HenonGenerator()) {

    // @formatter:off
    private val spnA            :   Spinner<Double>   by fxid()
    private val deltaAProperty  =   SimpleIntegerProperty(this, "deltaAlpha"    , 1     )
    private val aValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 2.0, 1.4, maxDelta)

    private val spnB            :   Spinner<Double>   by fxid()
    private val deltaBProperty  =   SimpleIntegerProperty(this, "deltaBeta"    , 1     )
    private val bValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.3, maxDelta)
    // @formatter:on

    override fun refreshData(generator: HenonGenerator, iterations: Int): List<BiPoint> =
            generator.generate(BiPoint(x0Property.value, y0Property.value), spnA.value, spnB.value, iterations)

    override fun initializeControlsBi() {
        spnA.configureActions(aValueFactory, deltaAProperty, this::loadData)
        spnB.configureActions(bValueFactory, deltaBProperty, this::loadData)
    }

}
