package rafael.logistic.generator

import java.time.Instant

sealed class IteractionEvent {
    abstract val interaction: Int
    abstract val priorX: Double
    abstract val x: Double
    val instant = Instant.now()!!
}

data class StartingEvent(override val x: Double) : IteractionEvent() {
    override val interaction: Int = 0
    override val priorX: Double = Double.NaN
}

data class RunningEvent(override val interaction: Int, override val priorX: Double, override val x: Double) : IteractionEvent()

data class EndingEvent(override val interaction: Int) : IteractionEvent() {
    override val priorX: Double = Double.NaN
    override val x: Double = Double.NaN
}

typealias LogisticEventListener = (IteractionEvent) -> Unit
