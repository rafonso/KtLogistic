package rafael.logistic.generator

import java.time.Instant

abstract class IteractionEvent<T> {
    abstract val interaction: Int
    abstract val priorValue: T?
    abstract val value: T?
    val instant = Instant.now()!!
}

data class StartingEvent<T>(override val value: T) : IteractionEvent<T>() {
    override val interaction: Int = 0
    override val priorValue: T? = null
}

data class RunningEvent<T>(override val interaction: Int, override val priorValue: T, override val value: T) :
        IteractionEvent<T>()

data class EndingEvent<T>(override val interaction: Int) : IteractionEvent<T>() {
    override val priorValue: T? = null
    override val value: T? = null
}

typealias LogisticEventListener<T> = (IteractionEvent<T>) -> Unit
