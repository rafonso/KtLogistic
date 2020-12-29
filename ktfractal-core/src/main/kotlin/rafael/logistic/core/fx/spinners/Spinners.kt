package rafael.logistic.core.fx.spinners

import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.Event
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory
import javafx.scene.input.*
import rafael.logistic.core.fx.oneProperty
import tornadofx.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.reflect.KFunction1

/*
 * Configuração dos [Spinner]s
 */

internal const val MIN_STEP = 1
internal const val MAX_STEP = 10

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
    // @formatter:off
    when (event) {
        is ScrollEvent  -> {
            val delta = if (event.isControlDown) 10 else 1
            if (event.deltaY > 0) this.increment(delta)
            if (event.deltaY < 0) this.decrement(delta)
        }
        is KeyEvent     -> if (event.eventType == KeyEvent.KEY_PRESSED && event.isControlDown) {
            when (event.code) {
                KeyCode.UP      -> this.increment(10)
                KeyCode.DOWN    -> this.decrement(10)
                else            -> {
                    // Do nothinng
                }
            }
        }
    }
    // @formatter:on
}

internal fun handleIncrement(isControl: Boolean, enum: Enum<*>, stepProperty: IntegerProperty) {
    if (isControl) {
        incrementAction[enum]?.let { it(stepProperty) }
    }
}

/**
 * Copia o valor do [Spinner] para a [Clipboard área de transferência] teclando Ctrc + C ou Ctrl + Ins.
 *
 * @receiver [Spinner]
 */
private fun Spinner<*>.addCopyCapacity() {
    this.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
        if (event.isControlDown && (event.code == KeyCode.C || event.code == KeyCode.INSERT)) {
            Clipboard.getSystemClipboard().putString(this.value.toString())
        }
    }
}

/**
 * Configura o [SpinnerValueFactory] do [Spinner], ...
 *
 * @param valueFactory SpinnerValueFactory a ser cinculado ao Spinner
 * @param listener Ação a ser feita ao mudar o valor
 * @return [ChangeListener] chamando `action`.
 */
private fun Spinner<*>.bind(
    valueFactory: SpinnerValueFactory<*>,
    listener: ChangeListener<in Any>
): ChangeListener<out Any> {
    this.valueFactory = valueFactory
    this.addEventHandler(ScrollEvent.SCROLL, this::incrementValue)
    this.addEventHandler(KeyEvent.KEY_PRESSED, this::incrementValue)
    this.addCopyCapacity()

    this.valueProperty().addListener(listener)

    return listener
}

/**
 * Configura o [SpinnerValueFactory] do [Spinner], ...
 *
 * @param valueFactory SpinnerValueFactory a ser cinculado ao Spinner
 * @param action Ação a ser feita ao mudar o valor
 * @return [ChangeListener] chamando `action`.
 */
internal fun Spinner<*>.bind(valueFactory: SpinnerValueFactory<*>, action: () -> Unit): ChangeListener<out Any> =
    this.bind(valueFactory) { _: ObservableValue<out Any>?, _: Any, _: Any -> action() }

/**
 * Víncula os valores mínimos e máximos de  dois [DoubleSpinner]s.
 *
 * @param configuration configuração dos [DoubleSpinner]s vinculados
 * @param action Ação a ser feita ao alterar os valores dos Spinners
 */
fun configureMinMaxSpinners(configuration: LimitsSpinnersConfiguration, action: () -> Unit) {
    val deltaLimitProperty = oneProperty()
    val deltaStepProperty = (0.1).toProperty()
    deltaLimitProperty.onChange {
        deltaStepProperty.value = (0.1).pow(it)
    }

    configuration.spnMin.configureActions(
        configuration.minValueFactory,
        deltaLimitProperty, action
    )
    configuration.spnMax.configureActions(
        configuration.maxValueFactory,
        deltaLimitProperty, action
    )

    configuration.minValueFactory.maxProperty()
        .bind(DoubleProperty.doubleProperty(configuration.maxValueFactory.valueProperty()) - deltaStepProperty)
    configuration.minValueFactory.maxProperty().onChange {
        configuration.spnMin.changeSpinnerTooltip(deltaLimitProperty.value)
    }

    configuration.maxValueFactory.minProperty()
        .bind(DoubleProperty.doubleProperty(configuration.minValueFactory.valueProperty()) + deltaStepProperty)
    configuration.maxValueFactory.minProperty().onChange {
        configuration.spnMax.changeSpinnerTooltip(deltaLimitProperty.value)
    }
}

/**
 * Cria uma nova instância de [DoubleSpinnerValueFactory].
 *
 * @param min Valor Mínimo [DoubleSpinnerValueFactory#min]
 * @param max Valor Máximo  [DoubleSpinnerValueFactory#max]
 * @param initialValue [Valor Inicial][DoubleSpinnerValueFactory#initialValue]
 * @param amountToStepBy valor inicial do [passso][DoubleSpinnerValueFactory#amountToStepBy]
 */
fun doubleSpinnerValueFactory(min: Double, max: Double, initialValue: Double, amountToStepBy: Double) =
    DoubleSpinnerValueFactory(min, max, initialValue, amountToStepBy)

