package rafael.logistic.maps.tent

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import rafael.logistic.generator.EndingEvent
import rafael.logistic.generator.IteractionEvent
import rafael.logistic.generator.RunningEvent
import rafael.logistic.generator.StartingEvent
import rafael.logistic.view.IteractionChart
import rafael.logistic.view.configureActions
import tornadofx.*
import java.time.Duration
import java.time.Instant

private const val MAX_DELTA = 0.1

class TentView : View("Logistic Equation") {

    // @formatter:off
    override val root               : BorderPane        by fxml("/Tent.fxml")
    private  val spnMi              : Spinner<Double>   by fxid()
    private  val spnX0              : Spinner<Double>   by fxid()
    private  val spnIteractions     : Spinner<Int>      by fxid()
    private  val chart              : TentChart         by fxid()
    private  val iteractionsChart   : IteractionChart   by fxid()
    // @formatter:on

    // @formatter:off
    private val deltaMiProperty          =   SimpleIntegerProperty(this, "deltaMi"  , 1)
    private val miValueFactory           =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 2.0, 1.0, MAX_DELTA)

    private val deltaX0Property         =   SimpleIntegerProperty(this, "deltaX0"   , 1)
    private val x0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, MAX_DELTA)

    private val iteractionsValueFactory =   SpinnerValueFactory.IntegerSpinnerValueFactory(50, 2000, 100, 50)

    private val generator               =   TentGenerator()

    private var t0: Instant?            =   null

    private val logisticData            =   emptyList<Double>().toProperty()

    // @formatter:on

    init {
        generator.addStatusListener(this::dataGenerated)

        spnMi.configureActions(miValueFactory, deltaMiProperty, this::loadData)
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnIteractions.configureActions(iteractionsValueFactory, this::loadData)

        chart.miProperty.bind(spnMi.valueProperty())
        chart.dataProperty.bind(logisticData)
        iteractionsChart.bind(spnIteractions.valueProperty(), logisticData)

        loadData()
    }

    private fun dataGenerated(event: IteractionEvent) {
        when (event) {
            is StartingEvent -> t0 = event.instant
            is RunningEvent -> {

            }
            is EndingEvent -> {
                val t1 = Instant.now()
                val deltaT = Duration.between(t0, t1)
            }
        }
    }

    private fun loadData() {
        this.logisticData.value = generator.generate(spnX0.value, spnMi.value, spnIteractions.value)
    }

}
