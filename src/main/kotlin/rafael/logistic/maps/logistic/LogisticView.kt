package rafael.logistic.maps.logistic

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import rafael.logistic.generator.*
import rafael.logistic.view.IteractionChart
import rafael.logistic.view.configureActions
import tornadofx.*
import java.time.Duration
import java.time.Instant

private const val MAX_DELTA = 0.1

class LogisticView : View("Logistic Equation") {

    // @formatter:off
    override val root               : BorderPane        by fxml("/Logistic.fxml")
    private  val spnR               : Spinner<Double>   by fxid()
    private  val spnX0              : Spinner<Double>   by fxid()
    private  val spnIteractions     : Spinner<Int>      by fxid()
    private  val logisticChart      : LogisticChart     by fxid()
    private  val iteractionsChart   : IteractionChart   by fxid()
    // @formatter:on

    // @formatter:off
    private val deltaRProperty          =   SimpleIntegerProperty(this, "deltaR"    , 1     )
    private val rValueFactory           =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 4.0, 1.0, MAX_DELTA)

    private val deltaX0Property         =   SimpleIntegerProperty(this, "deltaX0"   , 1     )
    private val x0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, MAX_DELTA)

    private val iteractionsValueFactory =   SpinnerValueFactory.IntegerSpinnerValueFactory(50, 2000, 100, 50)

    private val generator               =   LogisticGenerator()

    private var t0: Instant?            =   null

    private val logisticData            =   emptyList<Double>().toProperty()

    // @formatter:on

    init {
        generator.addStatusListener(this::dataGenerated)

        spnR.configureActions(rValueFactory, deltaRProperty, this::loadData)
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnIteractions.configureActions(iteractionsValueFactory, this::loadData)

        logisticChart.bind(logisticData) {
            when(it) {
                is LogisticChart -> it.rProperty.bind(spnR.valueProperty())
            }
        }
        iteractionsChart.bind(spnIteractions.valueProperty(), logisticData)

        loadData()
    }

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

    private fun loadData() {
        this.logisticData.value = generator.generate(spnX0.value, spnR.value, spnIteractions.value)
    }

}
