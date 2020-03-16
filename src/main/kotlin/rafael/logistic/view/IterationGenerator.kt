package rafael.logistic.view

import javafx.geometry.Point2D

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

    private tailrec fun iterate(parameter: P, interactions: Int, interaction: Int, priorValue: T, values: List<T>): List<T> {
        val value = calculate(parameter, priorValue)

        return if (interaction == interactions) values + value
        else iterate(parameter, interactions, interaction + 1, value, values + value)
    }

    override fun generate(initialValue: T, parameter: P, interactions: Int): List<T> =
            iterate(parameter, interactions, 1, initialValue, listOf(initialValue))

}

typealias IterationGeneratorDouble<P> = IterationGeneratorBase<Double, P>

typealias IterationGeneratorBi<P> = IterationGeneratorBase<Point2D, P>
