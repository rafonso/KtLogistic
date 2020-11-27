package rafael.logistic.core.generation

/**
 * Interface marcadora dos parâmetros para a geração dos dados.
 */
interface IterationParameter

/**
 * Implementação de [IterationParameter] usada nas gerações em que não são necessários parâmetros.
 */
object NoParameter : IterationParameter

/**
 * Interface básica para geração dos dados.
 *
 * @param I Tipo do valor inicial
 * @param T Tipo do valor a ser retornado. Pode ser igual a [I].
 * @param P Tipo de Parâmetro
 */
interface IterationGenerator<I, T, P : IterationParameter> {

    /**
     * Gera os dados a partir do valor inicial.
     *
     * @param initialValue Valor inicial
     * @param parameter Parêmtros da geração de dados.
     * @param interactions Número máximo de interações.
     * @return Lista dos dados gerados.
     */
    fun generate(initialValue: I, parameter: P, interactions: Int): List<T>

}
