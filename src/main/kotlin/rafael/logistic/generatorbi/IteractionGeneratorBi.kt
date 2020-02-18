package rafael.logistic.generatorbi

import java.time.Instant
import kotlin.properties.Delegates

data class BiPoint(val x: Double, val y: Double) {

    companion object {
        val ZERO = BiPoint(0.0, 0.0)
        val NAN = BiPoint(Double.NaN, Double.NaN)
    }

    override fun toString(): String = "(%f, %f)".format(x, y)

}

interface IteractionParameterBi

abstract class IteractionGeneratorBi<P : IteractionParameterBi> {

    private val eventListeners = mutableListOf<LogisticEventListenerBi>()

    private val calculatingListeners = mutableListOf<(Boolean, Instant) -> Unit>()

    private fun notify(eventBi: IteractionEventBi) {
        eventListeners.forEach { it(eventBi) }
    }

    private var _calculating: Boolean by Delegates.observable(false) { _, _, newValue ->
        val t0 = Instant.now()
        calculatingListeners.forEach { it(newValue, t0) }
    }

    val calculating: Boolean
        get() = _calculating

    fun addStatusListener(listenerBi: LogisticEventListenerBi) {
        eventListeners.add(listenerBi)
    }

    fun addCalculatingListener(listener: (Boolean, Instant) -> Unit) {
        calculatingListeners.add(listener)
    }

    private tailrec fun iterate(parameter: P, interactions: Int, interaction: Int, priorPont: BiPoint, values: List<BiPoint>): List<BiPoint> {
        val point = calculate(parameter, priorPont)
        notify(RunningEventBi(interaction, priorPont, point))

        return if (interaction == interactions) values + point
        else iterate(parameter, interactions, interaction + 1, point, values + point)
    }

    abstract fun calculate(parameter: P, point: BiPoint): BiPoint

    fun generate(p0: BiPoint, parameter: P, interactions: Int): List<BiPoint> {
        notify(StartingEventBi(p0))
        _calculating = true
        return iterate(parameter, interactions, 1, p0, listOf(p0)).also {
            notify(EndingEventBi(interactions))
            _calculating = false
        }
    }

}