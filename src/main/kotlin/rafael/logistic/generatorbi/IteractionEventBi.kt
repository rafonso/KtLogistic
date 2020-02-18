package rafael.logistic.generatorbi

import java.time.Instant

sealed class IteractionEventBi() {
    abstract val interaction: Int
    abstract val priorPoint: BiPoint
    abstract val point: BiPoint
    val instant = Instant.now()!!
}

data class StartingEventBi(override val point: BiPoint) : IteractionEventBi() {
    override val interaction: Int = 0
    override val priorPoint: BiPoint = BiPoint.NAN
}

data class RunningEventBi(override val interaction: Int, override val point: BiPoint, override val priorPoint: BiPoint) : IteractionEventBi()

data class EndingEventBi(override val interaction: Int) : IteractionEventBi() {
    override val point: BiPoint = BiPoint.NAN
    override val priorPoint: BiPoint = BiPoint.NAN
}

typealias LogisticEventListenerBi = (IteractionEventBi) -> Unit
