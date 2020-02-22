package rafael.logistic.generator

import java.time.Instant
import kotlin.properties.Delegates

interface IteractionParameter

object NoParameter: IteractionParameter

abstract class IteractionGenerator<T, P : IteractionParameter> {

    private val eventListeners = mutableListOf<LogisticEventListener<T>>()

    private val calculatingListeners = mutableListOf<(Boolean, Instant) -> Unit>()

    private fun notify(event: IteractionEvent<T>) {
        eventListeners.forEach { it(event) }
    }

    private var _calculating: Boolean by Delegates.observable(false) { _, _, newValue ->
        val t0 = Instant.now()
        calculatingListeners.forEach { it(newValue, t0) }
    }

    val calculating: Boolean
        get() = _calculating

    fun addStatusListener(listener: LogisticEventListener<T>) {
        eventListeners.add(listener)
    }

    fun addCalculatingListener(listener: (Boolean, Instant) -> Unit) {
        calculatingListeners.add(listener)
    }

    private tailrec fun iterate(parameter: P, interactions: Int, interaction: Int, priorValue: T, values: List<T>): List<T> {
        val value = calculate(parameter, priorValue)
        notify(RunningEvent(interaction, priorValue, value))

        return if (interaction == interactions) values + value
        else iterate(parameter, interactions, interaction + 1, value, values + value)
    }

    abstract fun calculate(parameter: P, value: T): T

    fun generate(initialValue: T, parameter: P, interactions: Int): List<T> {
        notify(StartingEvent(initialValue))
        _calculating = true
        return iterate(parameter, interactions, 1, initialValue, listOf(initialValue)).also {
            notify(EndingEvent(interactions))
            _calculating = false
        }
    }
}

typealias IteractionGeneratorDouble<P> = IteractionGenerator<Double, P>

typealias IteractionGeneratorBi<P> = IteractionGenerator<BiPoint, P>
