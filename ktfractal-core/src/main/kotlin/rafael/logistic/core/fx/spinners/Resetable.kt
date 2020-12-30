package rafael.logistic.core.fx.spinners

/**
 * Interface que indica capacidade de um componente para retornar a seu(s) valor(es) iniciais.
 */
interface Resetable {

    /**
     * Restaura os valores de um Componente JavaFX para seu respectivo valor inicial.
     */
    fun resetValue()

}