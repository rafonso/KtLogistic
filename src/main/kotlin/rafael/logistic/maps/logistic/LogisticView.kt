package rafael.logistic.maps.logistic

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.view.IteractionChart
import rafael.logistic.view.ViewBase
import rafael.logistic.view.configureActions

private const val MAX_DELTA = 0.1

class LogisticView : ViewBase<Double, LogisticGenerator>("Logistic Equation", "Logistic", LogisticGenerator()) {

    // @formatter:off
    private  val spnR               :   Spinner<Double>   by fxid()
    private  val spnX0              :   Spinner<Double>   by fxid()

    private  val logisticChart      :   LogisticChart     by fxid()
    private  val iteractionsChart   :   IteractionChart   by fxid()

    private val deltaRProperty      =   SimpleIntegerProperty(this, "deltaR"    , 1     )
    private val rValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 4.0, 1.0, MAX_DELTA)

    private val deltaX0Property     =   SimpleIntegerProperty(this, "deltaX0"   , 1     )
    private val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, MAX_DELTA)
    // @formatter:on

    override fun initializeControls() {
        spnR.configureActions(rValueFactory, deltaRProperty, this::loadData)
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
    }

    override fun initializeCharts() {
        logisticChart.bind(logisticData) {
            when (it) {
                is LogisticChart -> it.rProperty.bind(spnR.valueProperty())
            }
        }
        iteractionsChart.bind(spnIteractions.valueProperty(), logisticData)
    }

    override fun refreshData(generator: LogisticGenerator, iterations: Int): List<Double> =
            generator.generate(spnX0.value, spnR.value, iterations)

}
