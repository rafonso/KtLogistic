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
 * @param T Tipo do valor a ser retornado. pode ser igual a [I]
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

/**
 * Gerador de dados em que tanto o tipo do valor inicial quanto o dos dados gerados são iguais.
 *
 * @param T Tipo dos dados iniciais e garados.
 * @param P Tipo de Parâmetro
 */
abstract class IterationGeneratorBase<T, P : IterationParameter> : IterationGenerator<T, T, P> {

    abstract fun calculate(parameter: P, value: T): T

    /**
     * Cria um [Array] do tipo [T] que será preenchido os dados a serem retornados.
     *
     * @param size tamanho do array
     * @return Array do tipo [T] com o amanho solicitado
     */
    protected abstract fun createArray(size: Int): Array<T>

    private tailrec fun iterate(parameter: P, i: Int, priorValue: T, values: Array<T>): List<T> {
        if (i >= values.size) {
            return values.toList()
        }

        val value = calculate(parameter, priorValue)
        values[i] = value

        return iterate(parameter, i + 1, value, values)
    }

    override fun generate(initialValue: T, parameter: P, interactions: Int): List<T> =
        iterate(parameter, 1, initialValue, createArray(interactions).also { arr ->
            arr[0] = initialValue
        })

}

/**
 * [IterationGenerator] que retorna um uma lista de [Double]
 */
abstract class IterationGeneratorDouble<P : IterationParameter> :IterationGeneratorBase<Double, P>() {

    override fun createArray(size: Int): Array<Double> = Array(size) { Double.NaN }

}

/**
 * [IterationGenerator] que retorna um uma lista de [BiDouble]
 */
abstract class IterationGeneratorBi<P : IterationParameter> :IterationGeneratorBase<BiDouble, P>() {

    override fun createArray(size: Int): Array<BiDouble> = Array(size) { BiDouble.NAN }

}
