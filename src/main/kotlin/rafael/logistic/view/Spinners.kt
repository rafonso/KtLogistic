package rafael.logistic.view

import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.event.Event
import javafx.geometry.Pos
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.input.*
import tornadofx.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sign
import kotlin.reflect.KFunction1

/**
 * Configuração dos [Spinner]s
 */

private const val MIN_STEP = 1
private const val MAX_STEP = 9

private fun IntegerProperty.incrementConditional() {
    this.value = min(MAX_STEP, this.value + 1)
}

private fun IntegerProperty.decrementConditional() {
    this.value = max(MIN_STEP, this.value - 1)
}

private val incrementAction: Map<Enum<*>, KFunction1<IntegerProperty, Unit>> = mapOf(
        // @formatter:off
        Pair(KeyCode    .RIGHT      , IntegerProperty::incrementConditional),
        Pair(KeyCode    .LEFT       , IntegerProperty::decrementConditional),
        Pair(MouseButton.PRIMARY    , IntegerProperty::decrementConditional),
        Pair(MouseButton.SECONDARY  , IntegerProperty::incrementConditional)
        // @formatter:on
)

private fun Spinner<*>.incrementValue(event: Event) {
    when (event) {
        is ScrollEvent -> {
            val delta = if (event.isControlDown) 10 else 1
            if (event.deltaY > 0) this.increment(delta)
            if (event.deltaY < 0) this.decrement(delta)
        }
        is KeyEvent    -> {
            if (event.eventType == KeyEvent.KEY_PRESSED) {
                if (event.isControlDown) {
                    if (event.code == KeyCode.UP) {
                        this.increment(10)
                    } else if (event.code == KeyCode.DOWN) {
                        this.decrement(10)
                    }
                }
            }
        }
    }
}

private fun handleIncrement(isControl: Boolean, enum: Enum<*>, stepProperty: IntegerProperty) {
    if (isControl) {
        incrementAction[enum]?.let { it(stepProperty) }
    }
}

private fun Spinner<Double>.stepChanged(step: Int) {
    runLater {
        with(this.valueFactory as SpinnerValueFactory.DoubleSpinnerValueFactory) {
            this.converter = SpinnerConverter(step)
            this.amountToStepBy = (0.1).pow(step)
            val strValue = DecimalFormat("#." + "#".repeat(step))
                    .apply { roundingMode = RoundingMode.DOWN }
                    .format(this.value).replace(",", ".")
            this.value = this.converter.fromString(strValue)
            this@stepChanged.editor.text = this.converter.toString(this.value)
        }
    }
}

/**
 * Troca o sinal [Spinner] se tanto o valor positivo quanto o negativo estiverem na faixa permitida.
 *
 * @param valueFactory [SpinnerValueFactory.DoubleSpinnerValueFactory] do Spinner
 */
private fun Spinner<Double>.configureInvertSignal(valueFactory: SpinnerValueFactory.DoubleSpinnerValueFactory) {

    fun hasSign() = (valueFactory.value.sign != 0.0)

    fun canSwap() = ((valueFactory.value > 0) && (-valueFactory.value > valueFactory.min)) ||
            ((valueFactory.value < 0) && (-valueFactory.value < valueFactory.max))


    fun invertSignal(correctControls: Boolean) {
        if (correctControls && hasSign() && canSwap()) {
            valueFactory.value = -valueFactory.value
        }
    }


    if (valueFactory.min.sign == valueFactory.max.sign) {
        return
    }

    this.addEventFilter(MouseEvent.MOUSE_CLICKED) { event ->
        invertSignal((event.button == MouseButton.SECONDARY) && (event.clickCount == 2))
    }
    this.addEventHandler(KeyEvent.KEY_TYPED) { event ->
        invertSignal(event.character == "-")
    }
}


fun Spinner<*>.bind(valueFactory: SpinnerValueFactory<*>, action: () -> Unit) {
    this.valueFactory = valueFactory
    this.addEventHandler(ScrollEvent.SCROLL, this::incrementValue)
    this.addEventHandler(KeyEvent.KEY_PRESSED, this::incrementValue)
    this.valueProperty().onChange { action() }
}

fun Spinner<Double>.configureActions(valueFactory: SpinnerValueFactory.DoubleSpinnerValueFactory,
                                     deltaProperty: IntegerProperty, action: () -> Unit) {
    this.bind(valueFactory, action)

    // Desabilita o Context Menu. Fonte: https://stackoverflow.com/questions/43124577/how-to-disable-context-menu-in-javafx
    this.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume)
    this.addEventFilter(MouseEvent.MOUSE_CLICKED) { handleIncrement(it.isControlDown, it.button, deltaProperty) }
    this.addEventHandler(KeyEvent.KEY_PRESSED) { handleIncrement(it.isControlDown, it.code, deltaProperty) }
    this.configureInvertSignal(valueFactory)
    deltaProperty.addListener(ChangeListener { _, _, newStep -> this.stepChanged(newStep.toInt()) })
    this.stepChanged(deltaProperty.value)
}

fun Spinner<Int>.configureActions(valueFactory: SpinnerValueFactory<Int>, action: () -> Unit) {
    this.bind(valueFactory, action)
    this.editor.alignment = Pos.CENTER_RIGHT
}

fun configureMinMaxSpinners(spnMin: Spinner<Double>, minValueFactory: SpinnerValueFactory.DoubleSpinnerValueFactory,
                            spnMax: Spinner<Double>, maxValueFactory: SpinnerValueFactory.DoubleSpinnerValueFactory,
                            deltaLimitProperty: IntegerProperty, deltaStepProperty: DoubleProperty, action: () -> Unit) {
    deltaLimitProperty.onChange {
        deltaStepProperty.value = (0.1).pow(it)
    }
    spnMin.configureActions(minValueFactory, deltaLimitProperty, action)
    spnMax.configureActions(maxValueFactory, deltaLimitProperty, action)
    minValueFactory.maxProperty().bind(
            Bindings.subtract(DoubleProperty.doubleProperty(maxValueFactory.valueProperty()), deltaStepProperty))
    maxValueFactory.minProperty().bind(
            Bindings.add(DoubleProperty.doubleProperty(minValueFactory.valueProperty()), deltaStepProperty))
}

fun doubleSpinnerValueFactory(min: Double, max: Double, initialValue: Double, amountToStepBy: Double) =
        SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue, amountToStepBy)
