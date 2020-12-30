package rafael.logistic.core.fx.spinners

import javafx.beans.NamedArg
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import kotlin.properties.Delegates

class IntSpinner(
    @NamedArg("min") min: Int,
    @NamedArg("max") max: Int,
    @NamedArg("initialValue") initialValue: Int,
    @NamedArg("amountToStepBy") amountToStepBy: Int
) : Spinner<Int>(min, max, initialValue, amountToStepBy), Resetable {

    constructor(): this(0, 0, 0, 0)

    private     var initialValue            by  Delegates.notNull<Int>()

    override fun resetValue() {
        this.valueFactory.value = this.initialValue
    }

    /**
     * Confuigura os [Spinner]s do tipo [Int]. Configurando a ação a ser feita ao mudar o valor e alinhado o texto do
     * mesmo à direita.
     *
     * @param valueFactory [SpinnerValueFactory] a ser usado
     * @param action ação a ser feita ao mudar o valor.
     */
    fun initialize(valueFactory: SpinnerValueFactory<Int>, action: () -> Unit) {
        this.configure(valueFactory, action)
        this.initialValue = super.getValue()
    }

}