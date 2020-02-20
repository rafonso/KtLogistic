package rafael.logistic.maps.gingerbreadman

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.IteractionChartBi
import rafael.logistic.view.MapChartBi
import rafael.logistic.view.configureActions
import tornadofx.*

private const val MAX_DELTA = 1.0

private const val MAX_X = 10.0

class GingerbreadmanView : View("Ginger bread man") {

    // @formatter:off
    override val root               : BorderPane        by fxml("/Gingerbreadman.fxml")
    private  val spnX0              : Spinner<Double>   by fxid()
    private  val spnY0              : Spinner<Double>   by fxid()
    private  val spnIteractions     : Spinner<Int>      by fxid()
    private  val chart              : MapChartBi        by fxid()
    private  val xIterationsChart   : IteractionChartBi by fxid()
    private  val yIterationsChart   : IteractionChartBi by fxid()
    // @formatter:on

    // @formatter:off
    private val deltaX0Property         =   SimpleIntegerProperty(this, "deltaX0"   , 0     )
    private val x0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(-MAX_X, MAX_X, 0.0, MAX_DELTA)

    private val deltaY0Property         =   SimpleIntegerProperty(this, "deltaY0"   , 0     )
    private val y0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(-MAX_X, MAX_X, 0.0, MAX_DELTA)

    private val iteractionsValueFactory =   SpinnerValueFactory.IntegerSpinnerValueFactory(50, 2000, 100, 50)

    private val generator               =   GingerbreadmanGenerator()

    private val logisticData            =   emptyList<BiPoint>().toProperty()

    // @formatter:on

    init {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        spnY0.configureActions(y0ValueFactory, deltaY0Property, this::loadData)
        spnIteractions.configureActions(iteractionsValueFactory, this::loadData)

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
        this.logisticData.value = generator.generate(BiPoint(spnX0.value, spnY0.value), spnIteractions.value)
    }

}
