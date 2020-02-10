package rafael.logistic.view

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.Event
import javafx.geometry.Pos
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.input.*
import javafx.scene.layout.BorderPane
import javafx.util.StringConverter
import tornadofx.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

private const val MAX_DELTA = 0.1
private const val MIN_STEP = 1
private const val MAX_STEP = 7
private const val X_STEPS = 100

class MainView : View("Logistic Equation") {

    // @formatter:off
    override val root               : BorderPane                by fxml("/Logistic.fxml")
    private  val spnR               : Spinner<Double>           by fxid()
    private  val spnX0              : Spinner<Double>           by fxid()
    private  val spnIteractions     : Spinner<Int>              by fxid()
    private  val logisticChart      : LineChart<Double, Double> by fxid()
    private  val iteractionsChart   : LineChart<Int   , Double> by fxid()
    private  val iteractionsXAxis   : NumberAxis                by fxid()
    // @formatter:on

    // @formatter:off
    private val deltaRProperty          =   SimpleIntegerProperty(this, "deltaR"     , 1        )
    private val rValueFactory           =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 4.0, 1.0, MAX_DELTA)

    private val deltaX0Property         =   SimpleIntegerProperty(this, "deltaX0"    , 1        )
    private val x0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, MAX_DELTA)

    private val iteractionsValueFactory =   SpinnerValueFactory.IntegerSpinnerValueFactory(50, 2000, 100, 50)

    // @formatter:on

    init {
        spnR.valueFactory = rValueFactory
        initScrollSpinner(spnR)
        initCtrlMouseSpinner(spnR, deltaRProperty)
        spnR.valueProperty().onChange { loadData() }

        spnX0.valueFactory = x0ValueFactory
        initScrollSpinner(spnX0)
        initCtrlMouseSpinner(spnX0, deltaX0Property)

        spnIteractions.valueFactory = iteractionsValueFactory
        initScrollSpinner(spnIteractions)
        spnIteractions.editor.alignment = Pos.CENTER_RIGHT
        spnIteractions.valueProperty().addListener { _, _, newValue ->
            iteractionsXAxis.upperBound = newValue.toDouble()
            iteractionsXAxis.tickUnit = newValue.toDouble() / 10
        }



        (logisticChart.xAxis    as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>
        (logisticChart.yAxis    as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>

        (iteractionsChart.yAxis as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>

        loadData()
    }

    private fun loadData() {
        logisticChart.data.clear()
        logisticChart.series("xy") {
            cssclass("line1")
            data = listOf(data(0.0, 0.0), data(1.0, 1.0)).observable()
        }
        logisticChart.series("parable") {
            data = (0..X_STEPS).map { it.toDouble() / X_STEPS }
                    .map { XYChart.Data(it, it * spnR.value * (1.0 - it)) }.observable()
        }
    }

    private fun initScrollSpinner(spinner: Spinner<*>) {
        spinner.setOnScroll { event ->
            val delta = if (event.isControlDown) 10 else 1

            if (event.deltaY > 0) spinner.increment(delta)
            if (event.deltaY < 0) spinner.decrement(delta)
        }
        spinner.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            if (event.isControlDown) {
                if (event.code == KeyCode.UP) {
                    spinner.increment(10)
                } else if (event.code == KeyCode.DOWN) {
                    spinner.increment(-10)
                }
            }
        }
    }

    private fun initCtrlMouseSpinner(spinner: Spinner<Double>, stepProperty: IntegerProperty) {
        // Desabilita o Context Menu. Fonte: https://stackoverflow.com/questions/43124577/how-to-disable-context-menu-in-javafx
        spinner.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume)
        spinner.addEventFilter(MouseEvent.MOUSE_CLICKED) { event ->
            if (event.isControlDown) {
                if (event.button == MouseButton.PRIMARY) {
                    stepProperty.value = max(MIN_STEP, stepProperty.value - 1)
                } else if (event.button == MouseButton.SECONDARY) {
                    stepProperty.value = min(MAX_STEP, stepProperty.value + 1)
                }
            }
        }
        spinner.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            if (event.isControlDown) {
                if (event.code == KeyCode.RIGHT) {
                    stepProperty.value = min(MAX_STEP, stepProperty.value + 1)
                } else if (event.code == KeyCode.LEFT) {
                    stepProperty.value = min(MAX_STEP, stepProperty.value - 1)
                }
            }
        }

        stepProperty.addListener(ChangeListener { _, _, newStep -> stepChanged(spinner, newStep.toInt()) })
        stepChanged(spinner, stepProperty.value)
    }

    private fun stepChanged(spinner: Spinner<Double>, step: Int) {
        runLater {
            with(spinner.valueFactory as SpinnerValueFactory.DoubleSpinnerValueFactory) {
                this.converter = SpinnerConverter(step)
                this.amountToStepBy = (0.1).pow(step)
                val strValue = DecimalFormat("#." + "#".repeat(step))
                        .apply { roundingMode = RoundingMode.DOWN }
                        .format(this.value).replace(",", ".")
                this.value = this.converter.fromString(strValue)
                spinner.editor.text = this.converter.toString(this.value)
            }
        }
    }


}


class SpinnerConverter(size: Int) : StringConverter<Double>() {

    private val format = "%.${size}f"

    override fun toString(value: Double?): String = if (value != null) format.format(value) else ""

    override fun fromString(string: String?): Double? = string?.toDoubleOrNull()

}