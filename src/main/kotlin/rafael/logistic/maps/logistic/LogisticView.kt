package rafael.logistic.maps.logistic

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.view.ViewDouble
import rafael.logistic.view.configureActions

class LogisticView : ViewDouble<LogisticGenerator, LogisticChart>("Logistic Equation", "Logistic", LogisticGenerator()) {

    // @formatter:off
    private val spnR            :   Spinner<Double>   by fxid()
    private val deltaRProperty  =   SimpleIntegerProperty(this, "deltaR"    , 1     )
    private val rValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 4.0, 1.0, maxDelta)
    // @formatter:on

    override fun refreshData(generator: LogisticGenerator, iterations: Int): List<Double> =
            generator.generate(x0Property.value, spnR.value, iterations)

    override fun initializeControlsDouble() {
        spnR.configureActions(rValueFactory, deltaRProperty, this::loadData)
    }

    override fun initializeAdditional() {
        chart.rProperty.bind(spnR.valueProperty())
    }

}
