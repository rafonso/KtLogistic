package rafael.logistic.core.fx.spinners

import javafx.beans.property.IntegerProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.Event
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.input.Clipboard
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.ScrollEvent
import tornadofx.*
import kotlin.math.max
import kotlin.math.min

/*
 * Configuração dos [Spinner]s
 */

internal const val MIN_STEP = 1
internal const val MAX_STEP = 10

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

internal fun IntegerProperty.incrementConditional() {
    this.value = min(MAX_STEP, this.value + 1)
}

internal fun IntegerProperty.decrementConditional() {
    this.value = max(MIN_STEP, this.value - 1)
}

/**
 * Configura o [SpinnerValueFactory] do [Spinner], ...
 *
 * @receiver [Spinner]
 * @param valueFactory SpinnerValueFactory a ser cinculado ao Spinner
 * @param action Ação a ser feita ao mudar o valor
 * @return [ChangeListener] chamando `action`.
 */
internal fun Spinner<*>.bind(valueFactory: SpinnerValueFactory<*>, action: () -> Unit): (ObservableValue<out Any>?, Any, Any) -> Unit {
    this.valueFactory = valueFactory

    this.addEventHandler(ScrollEvent.SCROLL, this::incrementValue)
    this.addEventHandler(KeyEvent.KEY_PRESSED, this::incrementValue)
    this.addCopyCapacity()

    val listener = { _: ObservableValue<out Any>?, _: Any, _: Any -> action() }
    this.valueProperty().addListener(listener)
    return listener
}
