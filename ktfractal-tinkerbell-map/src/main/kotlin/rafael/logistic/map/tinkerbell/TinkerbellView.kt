package rafael.logistic.map.tinkerbell

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.valueToString
import rafael.logistic.core.fx.view.ViewBi
import rafael.logistic.core.generation.BiDouble
import tornadofx.toProperty

class TinkerbellView : ViewBi<TinkerbellGenerator>("Tinkerbell", "Tinkerbell", TinkerbellGenerator()) {

    override val iniX0Spinner: Double
        get() = -0.72

    override val maxX0Spinner: Double
        get() = 1.5

    override val minX0Spinner: Double
        get() = -maxX0Spinner

    override val iniY0Spinner: Double
        get() = -0.64

    override val maxY0Spinner: Double
        get() = 1.0

    override val minY0Spinner: Double
        get() = -maxY0Spinner

    // @formatter:off
    private val spnA            :   Spinner<Double>   by fxid()
    private val deltaAProperty  =   1.toProperty()
    private val aValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.9, maxDelta)

    private val spnB            :   Spinner<Double>   by fxid()
    private val deltaBProperty  =   4.toProperty()
    private val bValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 1.0, -0.6013, maxDelta)

    private val spnC            :   Spinner<Double>   by fxid()
    private val deltaCProperty  =   1.toProperty()
    private val cValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(-4.0, 4.0, 2.0, maxDelta)

    private val spnD            :   Spinner<Double>   by fxid()
    private val deltaDProperty  =   1.toProperty()
    private val dValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, maxDelta)
    // @formatter:on

    override fun refreshData(generator: TinkerbellGenerator, iterations: Int): List<BiDouble> =
            generator.generate(BiDouble(x0Property.value, y0Property.value), spnA.value, spnB.value, spnC.value, spnD.value, iterations)

    override fun initializeControlsBi() {
        spnA.configureActions(aValueFactory, deltaAProperty, this::loadData)
        spnB.configureActions(bValueFactory, deltaBProperty, this::loadData)
        spnC.configureActions(cValueFactory, deltaCProperty, this::loadData)
        spnD.configureActions(dValueFactory, deltaDProperty, this::loadData)
    }

    override fun getImageName1(): String = "Tinkerbell" +
            ".a=${spnA.valueToString()}.b=${spnB.valueToString()}.c=${spnC.valueToString()}.d=${spnD.valueToString()}"

}
