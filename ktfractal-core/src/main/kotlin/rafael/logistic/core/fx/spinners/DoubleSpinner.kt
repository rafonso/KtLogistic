package rafael.logistic.core.fx.spinners

import javafx.beans.NamedArg
import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
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
import kotlin.math.pow
import kotlin.math.sign
import kotlin.reflect.KFunction1

private val incrementAction: Map<Enum<*>, KFunction1<IntegerProperty, Unit>> = mapOf(
    // @formatter:off
    Pair(KeyCode    .RIGHT      , IntegerProperty::incrementConditional),
    Pair(KeyCode    .LEFT       , IntegerProperty::decrementConditional),
    Pair(MouseButton.PRIMARY    , IntegerProperty::decrementConditional),
    Pair(MouseButton.SECONDARY  , IntegerProperty::incrementConditional)
    // @formatter:on
)

class DoubleSpinner(
    @NamedArg("min") min: Double,
    @NamedArg("max") max: Double,
    @NamedArg("initialValue") initialValue: Double,
    @NamedArg("amountToStepBy") amountToStepBy: Double
) : Spinner<Double>(min, max, initialValue, amountToStepBy) {

    constructor() : this(0.0, 0.0, 0.0, 0.0)

    // @formatter:off

    internal    val decimalPlacesProperty   =   oneProperty()
    private     var decimalPlaces           by  decimalPlacesProperty

    private     val doubleFactory           :   DoubleSpinnerValueFactory
        get() = super.getValueFactory()     as  DoubleSpinnerValueFactory

    // @formatter:on

    private fun handleIncrement(isControl: Boolean, enum: Enum<*>) {
        if (isControl) {
            incrementAction[enum]?.let { it(decimalPlacesProperty) }
        }
    }

    /**
     * Configura a troca do sinal do [Spinner] se tanto o valor positivo quanto o negativo estiverem na faixa permitida.
     */
    private fun configureInvertSignal() {

        fun hasSign() = (doubleFactory.value.sign != 0.0)

        fun canSwap() = ((doubleFactory.value > 0) && (-doubleFactory.value > doubleFactory.min)) ||
                ((doubleFactory.value < 0) && (-doubleFactory.value < doubleFactory.max))

        fun invertSignal(correctControls: Boolean) {
            if (correctControls && hasSign() && canSwap()) {
                doubleFactory.value = -doubleFactory.value
            }
        }

        // --------------------------------------------------------------

        if (doubleFactory.min.sign == doubleFactory.max.sign) {
            return
        }

        this.addEventFilter(MouseEvent.MOUSE_CLICKED) { event ->
            invertSignal(!event.isControlDown && (event.button == MouseButton.SECONDARY) && (event.clickCount == 2))
        }
        this.addEventHandler(KeyEvent.KEY_TYPED) { event ->
            invertSignal(event.character == "-")
        }
    }

    /**
     * Altera o "passo" do Spinner. O argumento definirá quantos algarismos aparecerão depois da vírgula e o
     * valor de [Spinner#amountToStepBy] será igual a 10 ^ (- step). Por exemplo se `step` for 4 então o valor de
     * [Spinner#amountToStepBy] será 0,0001 e o valor será representado por "0,1234".
     */
    private fun stepChanged() {
        runLater {
            val strValue = DecimalFormat("#." + "#".repeat(decimalPlaces))
                .apply { roundingMode = RoundingMode.DOWN }
                .format(doubleFactory.value).replace(",", ".")
            doubleFactory.value = doubleFactory.converter.fromString(strValue)
            editor.text = doubleFactory.converter.toString(doubleFactory.value)

            changeSpinnerTooltip()
        }
    }

    /**
     * Configura o [Tooltip] do [Spinner] conforme os passos são alterados
     */
    internal fun changeSpinnerTooltip() {
        // @formatter:off
        val converter   = doubleFactory.converter
        val strStep     = converter.toString(doubleFactory.amountToStepBy       )
        val str10Step   = converter.toString(doubleFactory.amountToStepBy * 10  )
        val strMax      = converter.toString(doubleFactory.max                  )
        val strMin      = converter.toString(doubleFactory.min                  )
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
        if (decimalPlaces > MIN_STEP) {
            sbTootip.append('\n')
                .append("Press CRTL + ${'\u2190'} or CTRL + LBM to change step from $strStep to $str10Step")
        }
        if (decimalPlaces < MAX_STEP) {
            val temp01Step = strStep.toMutableList()
            temp01Step[temp01Step.lastIndex] = '0'
            temp01Step.add('1')
            val str01Step = temp01Step.joinToString("")

            sbTootip.append('\n')
                .append("Press CRTL + ${'\u2192'} or CTRL + RBM to change step from $strStep to $str01Step")
        }
        if (doubleFactory.max > 0 && doubleFactory.min < 0) {
            sbTootip.append('\n').append("Press - or Double click with right button mouse to change signal")
        }

        this.tooltip.text = sbTootip.toString()
    }

    /**
     * Configura um [Spinner] de [Double]s
     *
     * @param valueFactory [DoubleSpinnerValueFactory] do Spinner
     */
    fun initialize(
        valueFactory: DoubleSpinnerValueFactory,
        initialDecimalPlaces: Int,
        action: () -> Unit
    ) {
        this.bind(valueFactory, action)

        doubleFactory.amountToStepByProperty()
            .bind(Bindings.createDoubleBinding({ (0.1).pow(decimalPlaces) }, decimalPlacesProperty))
        doubleFactory.converterProperty()
            .bind(Bindings.createObjectBinding({ LogisticConverter(decimalPlaces) }, decimalPlacesProperty))

        // Desabilita o Context Menu. Fonte: https://stackoverflow.com/questions/43124577/how-to-disable-context-menu-in-javafx
        this.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume)
        this.addEventFilter(MouseEvent.MOUSE_CLICKED) { handleIncrement(it.isControlDown, it.button) }
        this.addEventHandler(KeyEvent.KEY_PRESSED) { handleIncrement(it.isControlDown, it.code) }
        this.configureInvertSignal()

        decimalPlaces = initialDecimalPlaces

        decimalPlacesProperty.addListener { _, _, _ -> this.stepChanged() }
        this.tooltip = Tooltip()
        stepChanged()
    }

    /**
     * @return Valor do Spinner formatado de acordo com o [Converter][SpinnerValueFactory.converter] do mesmo.
     */
    fun valueToString(): String = this.valueFactory.converter.toString(this.value)

}

/**
 * Víncula os valores mínimos e máximos de  dois [DoubleSpinner]s.
 *
 * @param configuration configuração dos [DoubleSpinner]s vinculados
 * @param action Ação a ser feita ao alterar os valores dos Spinners
 */
fun configureMinMaxSpinners(configuration: LimitsSpinnersConfiguration, action: () -> Unit) {
    configuration.spnMin.initialize(configuration.minValueFactory, configuration.initialDecimalPlaces, action)
    configuration.spnMax.initialize(configuration.maxValueFactory, configuration.initialDecimalPlaces, action)

    configuration.spnMin.decimalPlacesProperty.bindBidirectional(configuration.spnMax.decimalPlacesProperty)

    configuration.minValueFactory.maxProperty()
        .bind(DoubleProperty.doubleProperty(configuration.maxValueFactory.valueProperty()) - configuration.minValueFactory.amountToStepByProperty())
    configuration.minValueFactory.maxProperty().onChange { configuration.spnMin.changeSpinnerTooltip() }

    configuration.maxValueFactory.minProperty()
        .bind(DoubleProperty.doubleProperty(configuration.minValueFactory.valueProperty()) + configuration.maxValueFactory.amountToStepByProperty())
    configuration.maxValueFactory.minProperty().onChange { configuration.spnMax.changeSpinnerTooltip() }
}
