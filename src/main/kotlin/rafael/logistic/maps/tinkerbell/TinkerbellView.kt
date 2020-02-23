package rafael.logistic.maps.tinkerbell

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.view.ViewBi
import rafael.logistic.view.configureActions

class TinkerbellView : ViewBi<TinkerbellGenerator>("Tinkerbell", "Tinkerbell", TinkerbellGenerator()) {

    override val maxX0Spinner: Double
        get() = 1.5

    override val minX0Spinner: Double
        get() = -maxX0Spinner

    override val maxY0Spinner: Double
        get() = 1.0

    override val minY0Spinner: Double
        get() = -maxY0Spinner

    // @formatter:off
    private val spnA            :   Spinner<Double>   by fxid()
    private val deltaAProperty  =   SimpleIntegerProperty(this, "deltaAlpha"    , 1     )
    private val aValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, maxDelta)

    private val spnB            :   Spinner<Double>   by fxid()
    private val deltaBProperty  =   SimpleIntegerProperty(this, "deltaBeta"    , 1     )
    private val bValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 1.0, 0.6, maxDelta)

    private val spnC            :   Spinner<Double>   by fxid()
    private val deltaCProperty  =   SimpleIntegerProperty(this, "deltaC"    , 1     )
    private val cValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-4.0, 4.0, 2.0, maxDelta)

    private val spnD            :   Spinner<Double>   by fxid()
    private val deltaDProperty  =   SimpleIntegerProperty(this, "deltaD"    , 1     )
    private val dValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, maxDelta)
    // @formatter:on

    override fun refreshData(generator: TinkerbellGenerator, iterations: Int): List<BiPoint> =
            generator.generate(BiPoint(x0Property.value, y0Property.value), spnA.value, spnB.value, spnC.value, spnD.value, iterations)

    override fun initializeControlsBi() {
        spnA.configureActions(aValueFactory, deltaAProperty, this::loadData)
        spnB.configureActions(bValueFactory, deltaBProperty, this::loadData)
        spnC.configureActions(cValueFactory, deltaCProperty, this::loadData)
        spnD.configureActions(dValueFactory, deltaDProperty, this::loadData)
    }

}
