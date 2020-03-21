package rafael.logistic.view

import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.Event
import javafx.geometry.Pos
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory
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

const val MIN_STEP = 1
const val MAX_STEP = 10

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

/**
 * Altera o "passo" do Spinner. O argumento definirá quantos algarismos aparecerão depois da vírgula e o
 * valor de [Spinner#amountToStepBy] será igual a 10 ^ (- step). Por exemplo se `step` for 4 então o valor de
 * [Spinner#amountToStepBy] será 0,0001 e o valor será representado por "0,1234".
 *
 * @param step Passo do Spinner.
 */
private fun Spinner<Double>.stepChanged(step: Int) {
    runLater {
        with(this.valueFactory as DoubleSpinnerValueFactory) {
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
private fun Spinner<Double>.configureInvertSignal(valueFactory: DoubleSpinnerValueFactory) {

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
fun Spinner<*>.bind(valueFactory: SpinnerValueFactory<*>, listener: ChangeListener<in Any>): ChangeListener<out Any> {
    this.valueFactory = valueFactory
    this.addEventHandler(ScrollEvent.SCROLL, this::incrementValue)
    this.addEventHandler(KeyEvent.KEY_PRESSED, this::incrementValue)

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
fun Spinner<*>.bind(valueFactory: SpinnerValueFactory<*>, action: () -> Unit): ChangeListener<out Any> = this.bind(valueFactory, ChangeListener { _: ObservableValue<out Any>?, _: Any, _: Any -> action() })

/**
 * Configura um [Spinner] de [Double]s
 *
 * @param valueFactory [DoubleSpinnerValueFactory] do Spinner
 * @param deltaProperty
 */
fun Spinner<Double>.configureActions(valueFactory: DoubleSpinnerValueFactory,
                                     deltaProperty: IntegerProperty, action: () -> Unit): ChangeListener<*> {
    val listener: ChangeListener<*> = this.bind(valueFactory, action)

    // Desabilita o Context Menu. Fonte: https://stackoverflow.com/questions/43124577/how-to-disable-context-menu-in-javafx
    this.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume)
    this.addEventFilter(MouseEvent.MOUSE_CLICKED) { handleIncrement(it.isControlDown, it.button, deltaProperty) }
    this.addEventHandler(KeyEvent.KEY_PRESSED) { handleIncrement(it.isControlDown, it.code, deltaProperty) }
    this.configureInvertSignal(valueFactory)
    deltaProperty.addListener { _, _, newStep -> this.stepChanged(newStep.toInt()) }
    this.stepChanged(deltaProperty.value)

    return listener
}

/**
 * Confuigura os [Spinner]s do tipo [Int]. Configurando a ação a ser feita ao mudar o valor e alinhado o texto do
 * mesmo à direita.
 *
 * @param valueFactory [SpinnerValueFactory] a ser usado
 * @param action ação a ser feita ao mudar o valor.
 * @return [ChangeListener] "embalando" `action`
 */
fun Spinner<Int>.configureActions(valueFactory: SpinnerValueFactory<Int>, action: () -> Unit): ChangeListener<*> =
        this.bind(valueFactory, action).also {
            this.editor.alignment = Pos.CENTER_RIGHT
        }

/**
 * Víncula os valores mínimos e máximos de  dois [Spinner]s.
 *
 * @param spnMin Spinner que definirá o valor mínimo
 * @param minValueFactory DoubleSpinnerValueFactory relacionado a `spnMin`
 * @param spnMax Spinner que definirá o valor máxnimo
 * @param maxValueFactory DoubleSpinnerValueFactory relacionado a `spnMax`
 * @param deltaLimitProperty
 * @param deltaStepProperty
 * @param action Ação a ser feita ao alterar os valores dos Spinners
 * @return [ChangeListener]s relacionados a `spnMin` e `spnMax` "embalando" `action`
 *
 */
fun configureMinMaxSpinners(spnMin: Spinner<Double>, minValueFactory: DoubleSpinnerValueFactory,
                            spnMax: Spinner<Double>, maxValueFactory: DoubleSpinnerValueFactory,
                            deltaLimitProperty: IntegerProperty, deltaStepProperty: DoubleProperty, action: () -> Unit): Pair<ChangeListener<*>, ChangeListener<*>> {
    deltaLimitProperty.onChange {
        deltaStepProperty.value = (0.1).pow(it)
    }

    val listenerSpnMin = spnMin.configureActions(minValueFactory, deltaLimitProperty, action)
    val listenerSpnMax = spnMax.configureActions(maxValueFactory, deltaLimitProperty, action)

    minValueFactory.maxProperty().bind(DoubleProperty.doubleProperty(maxValueFactory.valueProperty()) - deltaStepProperty)
    maxValueFactory.minProperty().bind(DoubleProperty.doubleProperty(minValueFactory.valueProperty()) + deltaStepProperty)

    return Pair(listenerSpnMin, listenerSpnMax)
}

/**
 * Cria uma nova instância de [DoubleSpinnerValueFactory].
 *
 * @param min Valor Mínimo [DoubleSpinnerValueFactory#min]
 * @param max Valor Máximo  [DoubleSpinnerValueFactory#max]
 * @param initialValue Valor Inicial  [DoubleSpinnerValueFactory#initialValue]
 * @param amountToStepBy valor do passso  [DoubleSpinnerValueFactory#amountToStepBy]
 */
fun doubleSpinnerValueFactory(min: Double, max: Double, initialValue: Double, amountToStepBy: Double) =
        DoubleSpinnerValueFactory(min, max, initialValue, amountToStepBy)

/**
 * Copia o valor do [Spinner] para a [Clipboard área de transferência] teclando Ctrc + C ou Ctrl + Ins.
 */
fun Spinner<*>.addCopyCapacity() {
    this.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
        if (event.isControlDown && (event.code == KeyCode.C || event.code == KeyCode.INSERT)) {
            Clipboard.getSystemClipboard().putString(this.value.toString())
        }
    }
}
