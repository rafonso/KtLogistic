package rafael.logistic.core.fx.spinners

import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.Event
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory
import javafx.scene.control.Tooltip
import javafx.scene.input.*
import rafael.logistic.core.fx.LogisticConverter
import rafael.logistic.core.fx.oneProperty
import tornadofx.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sign
import kotlin.reflect.KFunction1

/*
 * Configuração dos [Spinner]s
 */

/**
 * Representa um [Spinner] para valores [Double].
 */
typealias DoubleSpinner = Spinner<Double>

private const val MIN_STEP = 1
private const val MAX_STEP = 10

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

private fun handleIncrement(isControl: Boolean, enum: Enum<*>, stepProperty: IntegerProperty) {
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
 * Altera o "passo" do Spinner. O argumento definirá quantos algarismos aparecerão depois da vírgula e o
 * valor de [Spinner#amountToStepBy] será igual a 10 ^ (- step). Por exemplo se `step` for 4 então o valor de
 * [Spinner#amountToStepBy] será 0,0001 e o valor será representado por "0,1234".
 *
 * @param step Passo do Spinner.
 */
private fun DoubleSpinner.stepChanged(step: Int) {
    runLater {
        with(this.valueFactory as DoubleSpinnerValueFactory) {
            this.converter = LogisticConverter(step)
            this.amountToStepBy = (0.1).pow(step)
            val strValue = DecimalFormat("#." + "#".repeat(step))
                .apply { roundingMode = RoundingMode.DOWN }
                .format(this.value).replace(",", ".")
            this.value = this.converter.fromString(strValue)
            this@stepChanged.editor.text = this.converter.toString(this.value)

            changeSpinnerTooltip(this@stepChanged, this, step)
        }
    }
}

/**
 * Configura o [Tooltip] do [Spinner] conforme os passos são alterados
 *
 * @param spinner [Spinner] enfocado
 * @param doubleSpinnerValueFactory [DoubleSpinnerValueFactory] do spinner
 * @param step passo do Spinner
 */
private fun changeSpinnerTooltip(
    spinner: DoubleSpinner,
    doubleSpinnerValueFactory: DoubleSpinnerValueFactory,
    step: Int
) {
    // @formatter:off
    val converter   = doubleSpinnerValueFactory.converter
    val strStep     = converter.toString(doubleSpinnerValueFactory.amountToStepBy       )
    val str10Step   = converter.toString(doubleSpinnerValueFactory.amountToStepBy * 10  )
    val strMax      = converter.toString(doubleSpinnerValueFactory.max                  )
    val strMin      = converter.toString(doubleSpinnerValueFactory.min                  )
    // @formatter:on

    val sbTootip = StringBuilder(
        """
            Max value = $strMax
            Min value = $strMin
            Press ${'\u2191'} to increment $strStep
            Press ${'\u2193'} to decrement $strStep
            Press CRTL + ${'\u2191'} to increment $str10Step
            Press CRTL + ${'\u2193'} to decrement $str10Step)
            """.trimIndent()
    )
    if (step > MIN_STEP) {
        sbTootip.append('\n')
            .append("Press CRTL + ${'\u2190'} or CTRL + LBM to change step from $strStep to $str10Step")
    }
    if (step < MAX_STEP) {
        val temp01Step = strStep.toMutableList()
        temp01Step[temp01Step.lastIndex] = '0'
        temp01Step.add('1')
        val str01Step = temp01Step.joinToString("")

        sbTootip.append('\n')
            .append("Press CRTL + ${'\u2192'} or CTRL + RBM to change step from $strStep to $str01Step")
    }
    if (doubleSpinnerValueFactory.max > 0 && doubleSpinnerValueFactory.min < 0) {
        sbTootip.append('\n').append("Press - or Double click with right button mouse to change signal")
    }

    spinner.tooltip.text = sbTootip.toString()
}

/**
 * Troca o sinal [Spinner] se tanto o valor positivo quanto o negativo estiverem na faixa permitida.
 *
 * @param valueFactory [SpinnerValueFactory.DoubleSpinnerValueFactory] do Spinner
 */
private fun DoubleSpinner.configureInvertSignal(valueFactory: DoubleSpinnerValueFactory) {

    fun hasSign() = (valueFactory.value.sign != 0.0)

    fun canSwap() = ((valueFactory.value > 0) && (-valueFactory.value > valueFactory.min)) ||
            ((valueFactory.value < 0) && (-valueFactory.value < valueFactory.max))

    fun invertSignal(correctControls: Boolean) {
        if (correctControls && hasSign() && canSwap()) {
            valueFactory.value = -valueFactory.value
        }
    }

    // --------------------------------------------------------------

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
 * @return Valor do Spinner formatado de acordo com o [Converter][SpinnerValueFactory.converter] do mesmo.
 */
fun DoubleSpinner.valueToString(): String = this.valueFactory.converter.toString(this.value)

/**
 * Configura um [Spinner] de [Double]s
 *
 * @receiver [Spinner]
 * @param valueFactory [DoubleSpinnerValueFactory] do Spinner
 * @param deltaProperty
 */
fun DoubleSpinner.configureActions(
    valueFactory: DoubleSpinnerValueFactory,
    deltaProperty: IntegerProperty,
    action: () -> Unit
): ChangeListener<*> {
    val listener: ChangeListener<*> = this.bind(valueFactory, action)

    // Desabilita o Context Menu. Fonte: https://stackoverflow.com/questions/43124577/how-to-disable-context-menu-in-javafx
    this.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume)
    this.addEventFilter(MouseEvent.MOUSE_CLICKED) { handleIncrement(it.isControlDown, it.button, deltaProperty) }
    this.addEventHandler(KeyEvent.KEY_PRESSED) { handleIncrement(it.isControlDown, it.code, deltaProperty) }
    this.configureInvertSignal(valueFactory)
    deltaProperty.addListener { _, _, newStep -> this.stepChanged(newStep.toInt()) }
    this.tooltip = Tooltip()
    this.stepChanged(deltaProperty.value)

    return listener
}

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
        changeSpinnerTooltip(configuration.spnMin, configuration.minValueFactory, deltaLimitProperty.value)
    }

    configuration.maxValueFactory.minProperty()
        .bind(DoubleProperty.doubleProperty(configuration.minValueFactory.valueProperty()) + deltaStepProperty)
    configuration.maxValueFactory.minProperty().onChange {
        changeSpinnerTooltip(configuration.spnMax, configuration.maxValueFactory, deltaLimitProperty.value)
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

