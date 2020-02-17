package rafael.logistic.generator

import java.time.Instant
import kotlin.properties.Delegates

interface IteractionParameter

abstract class IteractionGenerator<P : IteractionParameter> {

    private val eventListeners = mutableListOf<LogisticEventListener>()

    private val calculatingListeners = mutableListOf<(Boolean, Instant) -> Unit>()

    protected fun notify(event: IteractionEvent) {
        eventListeners.forEach { it(event) }
    }

    protected var _calculating: Boolean by Delegates.observable(false) { _, _, newValue ->
        val t0 = Instant.now()
        calculatingListeners.forEach { it(newValue, t0) }
    }

    val calculating: Boolean
        get() = _calculating

    fun addStatusListener(listener: LogisticEventListener) {
        eventListeners.add(listener)
    }

    fun addCalculatingListener(listener: (Boolean, Instant) -> Unit) {
        calculatingListeners.add(listener)
    }

    private tailrec fun iterate(parameter: P, interactions: Int, interaction: Int, xPrior: Double, values: List<Double>): List<Double> {
        val x = calculate(parameter, xPrior)
        notify(RunningEvent(interaction, xPrior, x))

        return if (interaction == interactions) values + x
        else iterate(parameter, interactions, interaction + 1, x, values + x)
    }

    protected abstract fun calculate(parameter: P, xPrior: Double): Double

    fun generate(x0: Double, parameter: P, interactions: Int): List<Double> {
        notify(StartingEvent(x0))
        _calculating = true
        return iterate(parameter, interactions, 1, x0, listOf(x0)).also {
            notify(EndingEvent(interactions))
            _calculating = false
        }
    }

}