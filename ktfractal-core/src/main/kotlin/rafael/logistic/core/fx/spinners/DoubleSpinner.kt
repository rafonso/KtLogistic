package rafael.logistic.core.fx.spinners

import javafx.beans.NamedArg
import javafx.beans.property.IntegerProperty
import javafx.beans.value.ChangeListener
import javafx.event.Event
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory
import javafx.scene.control.Tooltip
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import rafael.logistic.core.fx.LogisticConverter
import tornadofx.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.sign

class DoubleSpinner(
    @NamedArg("min") min: Double,
    @NamedArg("max") max: Double,
    @NamedArg("initialValue") initialValue: Double,
    @NamedArg("amountToStepBy") amountToStepBy: Double
) : Spinner<Double>(min, max, initialValue, amountToStepBy) {

    constructor() : this(0.0, 0.0, 0.0, 0.0)

    /**
     * Troca o sinal [Spinner] se tanto o valor positivo quanto o negativo estiverem na faixa permitida.
     *
     * @param valueFactory [SpinnerValueFactory.DoubleSpinnerValueFactory] do Spinner
     */
    private fun configureInvertSignal(valueFactory: DoubleSpinnerValueFactory) {

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
     * Configura o [Tooltip] do [Spinner] conforme os passos são alterados
     *
     * @param step passo do Spinner
     */
    internal fun changeSpinnerTooltip(step: Int) {
        // @formatter:off
        val doubleSpinnerValueFactory = super.getValueFactory() as DoubleSpinnerValueFactory
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

        this.tooltip.text = sbTootip.toString()
    }

    /**
     * Altera o "passo" do Spinner. O argumento definirá quantos algarismos aparecerão depois da vírgula e o
     * valor de [Spinner#amountToStepBy] será igual a 10 ^ (- step). Por exemplo se `step` for 4 então o valor de
     * [Spinner#amountToStepBy] será 0,0001 e o valor será representado por "0,1234".
     *
     * @param step Passo do Spinner.
     */
    private fun stepChanged(step: Int) {
        runLater {
            with(this.valueFactory as DoubleSpinnerValueFactory) {
                this.converter = LogisticConverter(step)
                this.amountToStepBy = (0.1).pow(step)
                val strValue = DecimalFormat("#." + "#".repeat(step))
                    .apply { roundingMode = RoundingMode.DOWN }
                    .format(this.value).replace(",", ".")
                this.value = this.converter.fromString(strValue)
                editor.text = this.converter.toString(this.value)

                changeSpinnerTooltip(step)
            }
        }
    }

    /**
     * Configura um [Spinner] de [Double]s
     *
     * @receiver [Spinner]
     * @param valueFactory [DoubleSpinnerValueFactory] do Spinner
     * @param deltaProperty
     */
    fun configureActions(
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
     * @return Valor do Spinner formatado de acordo com o [Converter][SpinnerValueFactory.converter] do mesmo.
     */
    fun valueToString(): String = this.valueFactory.converter.toString(this.value)


}