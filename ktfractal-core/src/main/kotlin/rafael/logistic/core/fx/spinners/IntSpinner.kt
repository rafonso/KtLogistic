package rafael.logistic.core.fx.spinners

import javafx.beans.NamedArg
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory

class IntSpinner(
    @NamedArg("min") min: Int,
    @NamedArg("max") max: Int,
    @NamedArg("initialValue") initialValue: Int,
    @NamedArg("amountToStepBy") amountToStepBy: Int
) : Spinner<Int>(min, max, initialValue, amountToStepBy) {

    constructor(): this(0, 0, 0, 0)

    /**
     * Confuigura os [Spinner]s do tipo [Int]. Configurando a ação a ser feita ao mudar o valor e alinhado o texto do
     * mesmo à direita.
     *
     * @param valueFactory [SpinnerValueFactory] a ser usado
     * @param action ação a ser feita ao mudar o valor.
     */
    fun initialize(valueFactory: SpinnerValueFactory<Int>, action: () -> Unit) {
        this.bind(valueFactory, action)
    }

}