package rafael.logistic.generator

import java.time.Instant

sealed class IterationEvent<I, T> {
    abstract val interaction: Int
    abstract val priorValue: T?
    abstract val value: T?
    val instant = Instant.now()!!
}

data class StartingEvent<I, T>(val initialValue: I) : IterationEvent<I, T>() {
    override val interaction: Int = 0
    override val value: T? = null
    override val priorValue: T? = null
}

data class RunningEvent<I, T>(override val interaction: Int, override val priorValue: T, override val value: T) :
        IterationEvent<I, T>()

data class EndingEvent<I, T>(override val interaction: Int) : IterationEvent<I, T>() {
    override val priorValue: T? = null
    override val value: T? = null
}

typealias LogisticEventListener<I, T> = (IterationEvent<I, T>) -> Unit
