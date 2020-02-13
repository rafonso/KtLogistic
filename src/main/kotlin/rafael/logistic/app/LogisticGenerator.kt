package rafael.logistic.app

import java.time.Instant

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

data class EndingEvent(override val interaction: Int, override val priorX: Double, override val x: Double) : LogisticEvent()

typealias LogisticEventListener = (LogisticEvent) -> Unit

class LogisticGenerator {

    private val listeners = mutableListOf<LogisticEventListener>()

    private fun notify(event: LogisticEvent) {
        listeners.forEach { it(event) }
    }

    private tailrec fun iterate(r: Double, interactions: Int, interaction: Int, xPrior: Double, values: List<Double>): List<Double> {
        val x = xPrior * r * (1.0 - xPrior)
        if (interaction == interactions) {
            notify(EndingEvent(interaction, xPrior, x))
            return values + x
        }

        notify(RunningEvent(interaction, xPrior, x))
        return iterate(r, interactions, interaction + 1, x, values + x)
    }

    fun generate(x0: Double, r: Double, interactions: Int): List<Double> {
        notify(StartingEvent(x0))
        return iterate(r, interactions, 1, x0, listOf(x0))
    }

    fun addStatusListener(listener: LogisticEventListener) {
        listeners.add(listener)
    }

}