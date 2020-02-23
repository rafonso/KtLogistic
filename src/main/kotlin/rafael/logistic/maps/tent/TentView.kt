package rafael.logistic.maps.tent

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.EndingEvent
import rafael.logistic.generator.IterationEvent
import rafael.logistic.generator.RunningEvent
import rafael.logistic.generator.StartingEvent
import rafael.logistic.view.IteractionChart
import rafael.logistic.view.ViewBase
import rafael.logistic.view.configureActions
import java.time.Duration
import java.time.Instant

private const val MAX_DELTA = 0.1

class TentView : ViewBase<Double, TentGenerator>("Logistic Equation", "Tent", TentGenerator()) {

    // @formatter:off
    private  val spnMi              : Spinner<Double>   by fxid()
    private  val spnX0              : Spinner<Double>   by fxid()

    private  val chart              : TentChart         by fxid()
    private  val iteractionsChart   : IteractionChart   by fxid()
    // @formatter:on

    // @formatter:off
    private val deltaMiProperty          =   SimpleIntegerProperty(this, "deltaMi"  , 1)
    private val miValueFactory           =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 2.0, 1.0, MAX_DELTA)

    private val deltaX0Property         =   SimpleIntegerProperty(this, "deltaX0"   , 1)
    private val x0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, MAX_DELTA)

    private var t0: Instant?            =   null
    // @formatter:on

    private fun dataGenerated(event: IterationEvent<Double>) {
        when (event) {
            is StartingEvent -> t0 = event.instant
            is RunningEvent  -> {

            }
            is EndingEvent   -> {
                val t1 = Instant.now()
                val deltaT = Duration.between(t0, t1)
            }
        }
    }

    override fun initializeControls() {
        spnMi.configureActions(miValueFactory, deltaMiProperty, this::loadData)
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
    }

    override fun initializeCharts() {
        chart.bind(logisticData) {
            when (it) {
                is TentChart -> it.miProperty.bind(spnMi.valueProperty())
            }
        }
        chart.dataProperty.bind(logisticData)
        iteractionsChart.bind(spnIteractions.valueProperty(), logisticData)
    }

    override fun initializeAdditional() {
        generator.addStatusListener(this::dataGenerated)
    }

    override fun refreshData(generator: TentGenerator, iterations: Int): List<Double> =
            generator.generate(spnX0.value, spnMi.value, iterations)

}
