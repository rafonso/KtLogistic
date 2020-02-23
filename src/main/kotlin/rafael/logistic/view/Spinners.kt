package rafael.logistic.view

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
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

private const val MIN_STEP = 1
private const val MAX_STEP = 9

private fun Spinner<*>.configureSpinnerIncrement() {
    this.setOnScroll { event ->
        val delta = if (event.isControlDown) 10 else 1

        if (event.deltaY > 0) this.increment(delta)
        if (event.deltaY < 0) this.decrement(delta)
    }
    this.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
        if (event.isControlDown) {
            if (event.code == KeyCode.UP) {
                this.increment(10)
            } else if (event.code == KeyCode.DOWN) {
                this.increment(-10)
            }
        }
    }
}

private fun Spinner<Double>.configureSpinnerStep(stepProperty: IntegerProperty) {
    // Desabilita o Context Menu. Fonte: https://stackoverflow.com/questions/43124577/how-to-disable-context-menu-in-javafx
    this.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume)
    this.addEventFilter(MouseEvent.MOUSE_CLICKED) { event ->
        if (event.isControlDown) {
            if (event.button == MouseButton.PRIMARY) {
                stepProperty.value = max(MIN_STEP, stepProperty.value - 1)
            } else if (event.button == MouseButton.SECONDARY) {
                stepProperty.value = min(MAX_STEP, stepProperty.value + 1)
            }
        }
    }
    this.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
        if (event.isControlDown) {
            if (event.code == KeyCode.RIGHT) {
                stepProperty.value = min(MAX_STEP, stepProperty.value + 1)
            } else if (event.code == KeyCode.LEFT) {
                stepProperty.value = min(MAX_STEP, stepProperty.value - 1)
            }
        }
    }

    stepProperty.addListener(ChangeListener { _, _, newStep -> this.stepChanged(newStep.toInt()) })
    this.stepChanged(stepProperty.value)
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

private fun Spinner<*>.bind(valueFactory: SpinnerValueFactory<*>, action: () -> Unit) {
    this.valueFactory = valueFactory
    this.configureSpinnerIncrement()
    this.valueProperty().onChange { action() }
}

fun Spinner<Double>.configureActions(valueFactory: SpinnerValueFactory.DoubleSpinnerValueFactory,
                                     deltaProperty: IntegerProperty, action: () -> Unit) {
    this.bind(valueFactory, action)
    this.configureSpinnerStep(deltaProperty)
}

fun Spinner<Int>.configureActions(valueFactory: SpinnerValueFactory.IntegerSpinnerValueFactory, action: () -> Unit) {
    this.bind(valueFactory, action)
    this.editor.alignment = Pos.CENTER_RIGHT
}
