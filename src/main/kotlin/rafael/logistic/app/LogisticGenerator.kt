package rafael.logistic.app

import java.time.Instant
import kotlin.properties.Delegates

sealed class LogisticEvent {
    abstract val interaction: Int
    abstract val priorX: Double
    abstract val x: Double
    val instant = Instant.now()!!
}

data class StartingEvent(override val x: Double) : LogisticEvent() {
    override val interaction: Int = 0
    override val priorX: Double = Double.NaN
}

data class RunningEvent(override val interaction: Int, override val priorX: Double, override val x: Double) : LogisticEvent()

data class EndingEvent(override val interaction: Int) : LogisticEvent() {
    override val priorX: Double = Double.NaN
    override val x: Double = Double.NaN
}

typealias LogisticEventListener = (LogisticEvent) -> Unit



class LogisticGenerator {

    private val eventListeners = mutableListOf<LogisticEventListener>()

    private val calculatingListeners = mutableListOf<(Boolean, Instant) -> Unit>()

    private fun notify(event: LogisticEvent) {
        eventListeners.forEach { it(event) }
    }

    private var _calculating: Boolean by Delegates.observable(false) { _, _, newValue ->
        val t0 = Instant.now()
        calculatingListeners.forEach { it(newValue, t0) }
    }

    val calculating: Boolean
        get() = _calculating

    private tailrec fun iterate(r: Double, interactions: Int, interaction: Int, xPrior: Double, values: List<Double>): List<Double> {
        val x = xPrior * r * (1.0 - xPrior)
        notify(RunningEvent(interaction, xPrior, x))

        return if (interaction == interactions) values + x
        else iterate(r, interactions, interaction + 1, x, values + x)
    }

    fun generate(x0: Double, r: Double, interactions: Int): List<Double> {
        notify(StartingEvent(x0))
        _calculating = true
        return iterate(r, interactions, 1, x0, listOf(x0)).also {
            notify(EndingEvent(interactions))
            _calculating = false
        }
    }

    fun addStatusListener(listener: LogisticEventListener) {
        eventListeners.add(listener)
    }

    fun addCalculatingListener(listener: (Boolean, Instant) -> Unit) {
        calculatingListeners.add(listener)
    }

}