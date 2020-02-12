package rafael.logistic.view

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleBooleanProperty
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
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import javafx.util.StringConverter
import rafael.logistic.app.*
import tornadofx.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

private const val MAX_DELTA = 0.1
private const val MIN_STEP = 1
private const val MAX_STEP = 7
private const val X_STEPS = 100

typealias Coord = Pair<Double, Double>

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
    private val deltaRProperty          =   SimpleIntegerProperty(this, "deltaR"    , 1     )
    private val rValueFactory           =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 4.0, 1.0, MAX_DELTA)

    private val deltaX0Property         =   SimpleIntegerProperty(this, "deltaX0"   , 1     )
    private val x0ValueFactory          =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, MAX_DELTA)

    private val iteractionsValueFactory =   SpinnerValueFactory.IntegerSpinnerValueFactory(50, 2000, 100, 50)

    private val generator               =   LogisticGenerator()

    private val runningProperty         =   SimpleBooleanProperty(this, "running"   , false )

    private var t0: Instant?            =   null

    private val logisticChartBackgound  =   logisticChart.lookup(".chart-plot-background")

    private val logisticData            =   emptyList<Double>().toProperty()

    // @formatter:on

    init {
        generator.addStatusListener(this::dataGenerated)

        spnR.valueFactory = rValueFactory
        initScrollSpinner(spnR)
        initCtrlMouseSpinner(spnR, deltaRProperty)
        spnR.valueProperty().onChange { loadData() }

        spnX0.valueFactory = x0ValueFactory
        initScrollSpinner(spnX0)
        initCtrlMouseSpinner(spnX0, deltaX0Property)
        spnX0.valueProperty().onChange { loadData() }

        spnIteractions.valueFactory = iteractionsValueFactory
        initScrollSpinner(spnIteractions)
        spnIteractions.editor.alignment = Pos.CENTER_RIGHT
        spnIteractions.valueProperty().addListener { _, _, newValue ->
            iteractionsXAxis.upperBound = newValue.toDouble()
            iteractionsXAxis.tickUnit = newValue.toDouble() / 10
            loadData()
        }

        (logisticChart.xAxis as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>
        (logisticChart.yAxis as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>
        (iteractionsChart.yAxis as NumberAxis).tickLabelFormatter = SpinnerConverter(2) as StringConverter<Number>

        logisticChart.boundsInParentProperty().onChange { refreshLogisticPlot() }

        logisticData.also { it.onChange { reloadPlots() } }
        loadData()
    }

    private fun Double.toLogisticXPos() = logisticChart.xAxis.getDisplayPosition(this)

    private fun Double.toLogisticYPos() = logisticChart.yAxis.getDisplayPosition(this)

    private tailrec fun generateLogisticData(itValues: Iterator<Double>, pt1: Pair<Double, Double>, shapes: List<Shape>): List<Shape> =
            if (!itValues.hasNext()) shapes // .also { println(it) }
            else {
                val pt2 = Pair(pt1.second, itValues.next())

                val c1 = Circle(pt1.first.toLogisticXPos(), pt1.second.toLogisticYPos(), 2.0).also {
                    it.fill = c("green")
                }
                val c2 = Circle(pt2.first.toLogisticXPos(), pt2.first.toLogisticYPos(), 2.0).also {
                    it.fill = c("blue")
                }
                generateLogisticData(itValues, pt2, shapes + c1 + c2)
            }

    private tailrec fun coordToShape(itCoord: ListIterator<Coord>, priorCoord: Coord, shapes: List<Shape>): List<Shape> =
            if (!itCoord.hasNext()) shapes
            else {
                val strColor = if (itCoord.nextIndex() % 2 == 0) "green" else "blue"
                val coord = itCoord.next()
                val c2 = Circle(coord.first.toLogisticXPos(), coord.first.toLogisticYPos(), 2.0).also {
                    it.fill = c(strColor)
                }
                coordToShape(itCoord, coord, shapes + c2)
            }

    private tailrec fun dataToCoord(itData: Iterator<Double>, priorX: Double, pairs: List<Coord>): List<Coord> =
            if (!itData.hasNext()) pairs
            else {
                val x = itData.next()
                dataToCoord(itData, x, pairs + Pair(priorX, x) + Pair(x, x))
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

    private fun dataGenerated(event: LogisticEvent) {
        when (event) {
            is StartingEvent -> t0 = event.instant
            is RunningEvent -> {

            }
            is EndingEvent -> {
                val t1 = Instant.now()
                val deltaT = Duration.between(t0, t1)
                println(deltaT)
            }
        }
    }

    private fun loadData() {
        this.logisticData.value = generator.generate(spnX0.value, spnR.value, spnIteractions.value)
    }


    private fun reloadPlots() {
        logisticChart.data.clear()
        logisticChart.series("xy") {
            cssclass("line1")
            data = listOf(data(0.0, 0.0), data(1.0, 1.0)).observable()
        }
        logisticChart.series("parable") {
            data = (0..X_STEPS).map { it.toDouble() / X_STEPS }
                    .map { XYChart.Data(it, it * spnR.value * (1.0 - it)) }.observable()
        }
        refreshLogisticPlot()

        iteractionsChart.data.clear()
        iteractionsChart.series("iteractions") {
            data = logisticData.value.mapIndexed { index, d -> XYChart.Data(index, d) }.observable()
        }
    }

    private fun refreshLogisticPlot() {
        logisticChartBackgound.replaceChildren()
        runLater {
            val data = logisticData.value
            val coords = (listOf(Pair(data[0], 0.0)) + (1 until data.size)
                    .flatMap { i -> listOf(Pair(data[i - 1], data[i]), Pair(data[i], data[i])) })
                    .map { (x, y) -> Pair(x.toLogisticXPos(), y.toLogisticYPos()) }
            (1 until coords.size)
                    .map { i -> Line(coords[i - 1].first, coords[i - 1].second, coords[i].first, coords[i].second).also { l ->
                        l.stroke = if(i % 2 == 0) c("green") else c("blue")
                    } }
                    .forEach { l -> logisticChartBackgound.add(l) }
        }
    }

}


class SpinnerConverter(size: Int) : StringConverter<Double>() {

    private val format = "%.${size}f"

    override fun toString(value: Double?): String = if (value != null) format.format(value) else ""

    override fun fromString(string: String?): Double? = string?.toDoubleOrNull()

}