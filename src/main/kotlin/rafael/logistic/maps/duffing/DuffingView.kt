package rafael.logistic.maps.duffing

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.IteractionChartBi
import rafael.logistic.view.MapChartBi
import rafael.logistic.view.configureSpinnerIncrement
import rafael.logistic.view.configureSpinnerStep
import tornadofx.*

private const val MAX_DELTA = 0.1

private const val MAX_X = 2.0

class DuffingView : View("Duffing") {

    // @formatter:off
    override val root               : BorderPane        by fxml("/Duffing.fxml")
    private  val spnA               : Spinner<Double>   by fxid()
    private  val spnB               : Spinner<Double>   by fxid()
    private  val spnX0              : Spinner<Double>   by fxid()
    private  val spnY0              : Spinner<Double>   by fxid()
    private  val spnIteractions     : Spinner<Int>      by fxid()
    private  val chart              : MapChartBi        by fxid()
    private  val xIterationsChart   : IteractionChartBi by fxid()
    private  val yIterationsChart   : IteractionChartBi by fxid()
    // @formatter:on

    // @formatter:off
    private val deltaAProperty          =   SimpleIntegerProperty(this, "deltaAlpha"    , 1     )
    private val aValueFactory           =   SpinnerValueFactory.DoubleSpinnerValueFactory(2.0, 3.0, 2.7, MAX_DELTA)

    private val deltaBProperty          =   SimpleIntegerProperty(this, "deltaBeta"    , 2     )
    private val bValueFactory           =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 0.3, 0.15, MAX_DELTA)

    private val deltaX0Property         =   SimpleIntegerProperty(this, "deltaX0"   , 1     )
    private val x0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(-MAX_X, MAX_X, 0.0, MAX_DELTA)

    private val deltaY0Property         =   SimpleIntegerProperty(this, "deltaY0"   , 1     )
    private val y0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(- 2 * MAX_X / 3, 2 * MAX_X / 3 , 0.0, MAX_DELTA)

    private val iteractionsValueFactory =   SpinnerValueFactory.IntegerSpinnerValueFactory(50, 2000, 100, 50)

    private val generator               =   DuffingGenerator()

    private val logisticData            =   emptyList<BiPoint>().toProperty()

    // @formatter:on

    init {
        spnA.valueFactory = aValueFactory
        spnA.configureSpinnerIncrement()
        spnA.configureSpinnerStep(deltaAProperty)
        spnA.valueProperty().onChange { loadData() }

        spnB.valueFactory = bValueFactory
        spnB.configureSpinnerIncrement()
        spnB.configureSpinnerStep(deltaBProperty)
        spnB.valueProperty().onChange { loadData() }

        spnX0.valueFactory = x0ValueFactory
        spnX0.configureSpinnerIncrement()
        spnX0.configureSpinnerStep(deltaX0Property)
        spnX0.valueProperty().onChange { loadData() }

        spnY0.valueFactory = y0ValueFactory
        spnY0.configureSpinnerIncrement()
        spnY0.configureSpinnerStep(deltaY0Property)
        spnY0.valueProperty().onChange { loadData() }

        spnIteractions.valueFactory = iteractionsValueFactory
        spnIteractions.configureSpinnerIncrement()
        spnIteractions.editor.alignment = Pos.CENTER_RIGHT
        spnIteractions.valueProperty().onChange { loadData() }

        chart.dataProperty.bind(logisticData)

        xIterationsChart.extractor = IteractionChartBi.extractorX
        xIterationsChart.iteractionsProperty.bind(spnIteractions.valueProperty())
        xIterationsChart.iteractionDataProperty.bind(logisticData)

        yIterationsChart.extractor = IteractionChartBi.extractorY
        yIterationsChart.iteractionsProperty.bind(spnIteractions.valueProperty())
        yIterationsChart.iteractionDataProperty.bind(logisticData)

        loadData()
    }

    private fun loadData() {
        this.logisticData.value = generator.generate(BiPoint(spnX0.value, spnY0.value), spnA.value, spnB.value, spnIteractions.value)
    }

}
