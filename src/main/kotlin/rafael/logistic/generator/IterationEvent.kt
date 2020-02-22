package rafael.logistic.generator

import java.time.Instant

sealed class IterationEvent<T> {
    abstract val interaction: Int
    abstract val priorValue: T?
    abstract val value: T?
    val instant = Instant.now()!!
}

data class StartingEvent<T>(override val value: T) : IterationEvent<T>() {
    override val interaction: Int = 0
    override val priorValue: T? = null
}

data class RunningEvent<T>(override val interaction: Int, override val priorValue: T, override val value: T) :
        IterationEvent<T>()

data class EndingEvent<T>(override val interaction: Int) : IterationEvent<T>() {
    override val priorValue: T? = null
    override val value: T? = null
}

typealias LogisticEventListener<T> = (IterationEvent<T>) -> Unit
