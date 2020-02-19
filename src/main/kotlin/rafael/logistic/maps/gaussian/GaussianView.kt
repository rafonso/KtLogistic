package rafael.logistic.maps.gaussian

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import rafael.logistic.generator.*
import rafael.logistic.view.IteractionChart
import rafael.logistic.view.configureSpinnerStep
import rafael.logistic.view.configureSpinnerIncrement
import tornadofx.*
import java.time.Duration
import java.time.Instant

private const val MAX_DELTA = 0.1

class GaussianView : View("Gaussian Equation") {

    // @formatter:off
    override val root               : BorderPane        by fxml("/Gaussian.fxml")
    private  val spnAlpha           : Spinner<Double>   by fxid()
    private  val spnBeta            : Spinner<Double>   by fxid()
    private  val spnX0              : Spinner<Double>   by fxid()
    private  val spnIteractions     : Spinner<Int>      by fxid()
    private  val gaussianChart      : GaussianChart     by fxid()
    private  val iteractionsChart   : IteractionChart   by fxid()
    // @formatter:on

    // @formatter:off
    private val deltaAlphaProperty      =   SimpleIntegerProperty(this, "deltaAlpha"    , 1     )
    private val alphaValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 1.0, MAX_DELTA)

    private val deltaBetaProperty       =   SimpleIntegerProperty(this, "deltaBeta"    , 1     )
    private val betaValueFactory        =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 0.0, 0.0, MAX_DELTA)

    private val deltaX0Property         =   SimpleIntegerProperty(this, "deltaX0"   , 1     )
    private val x0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(-1.0, 1.0, 0.0, MAX_DELTA)

    private val iteractionsValueFactory =   SpinnerValueFactory.IntegerSpinnerValueFactory(50, 2000, 100, 50)

    private val generator               =   GaussianGenerator()

    private var t0: Instant?            =   null

    private val logisticData            =   emptyList<Double>().toProperty()

    // @formatter:on

    init {
        generator.addStatusListener(this::dataGenerated)

        spnAlpha.valueFactory = alphaValueFactory
        spnAlpha.configureSpinnerIncrement()
        spnAlpha.configureSpinnerStep(deltaAlphaProperty)
        spnAlpha.valueProperty().onChange { loadData() }

        spnBeta.valueFactory = betaValueFactory
        spnBeta.configureSpinnerIncrement()
        spnBeta.configureSpinnerStep(deltaBetaProperty)
        spnBeta.valueProperty().onChange { loadData() }

        spnX0.valueFactory = x0ValueFactory
        spnX0.configureSpinnerIncrement()
        spnX0.configureSpinnerStep(deltaX0Property)
        spnX0.valueProperty().onChange { loadData() }

        spnIteractions.valueFactory = iteractionsValueFactory
        spnIteractions.configureSpinnerIncrement()
        spnIteractions.editor.alignment = Pos.CENTER_RIGHT
        spnIteractions.valueProperty().onChange { loadData() }

        gaussianChart.alphaProperty.bind(spnAlpha.valueProperty())
        gaussianChart.betaProperty.bind(spnBeta.valueProperty())
        gaussianChart.dataProperty.bind(logisticData)

        iteractionsChart.iteractionsProperty.bind(spnIteractions.valueProperty())
        iteractionsChart.iteractionDataProperty.bind(logisticData)

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
        this.logisticData.value = generator.generate(spnX0.value, spnAlpha.value, spnBeta.value, spnIteractions.value)
    }

}
