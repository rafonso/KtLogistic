package rafael.logistic.generator

import javafx.geometry.Point2D
import java.time.Instant
import kotlin.properties.Delegates

interface IterationParameter

object NoParameter : IterationParameter

abstract class IterationGenerator<I, T, P : IterationParameter> {

    private val eventListeners = mutableListOf<LogisticEventListener<I, T>>()

    private val calculatingListeners = mutableListOf<(Boolean, Instant) -> Unit>()

    protected fun notify(event: IterationEvent<I, T>) {
        eventListeners.forEach { it(event) }
    }

    private var _calculating: Boolean by Delegates.observable(false) { _, _, newValue ->
        val t0 = Instant.now()
        calculatingListeners.forEach { it(newValue, t0) }
    }

    val calculating: Boolean
        get() = _calculating

    fun addStatusListener(listener: LogisticEventListener<I, T>) {
        eventListeners.add(listener)
    }

    fun addCalculatingListener(listener: (Boolean, Instant) -> Unit) {
        calculatingListeners.add(listener)
    }

    protected abstract fun run(parameter: P, interactions: Int, initialValue: I): List<T> // =

    fun generate(initialValue: I, parameter: P, interactions: Int): List<T> {
        notify(StartingEvent(initialValue))
        _calculating = true
        return run(parameter, interactions, initialValue).also {
            notify(EndingEvent(interactions))
            _calculating = false
        }
    }
}

abstract class IterationGeneratorDouble<P : IterationParameter> : IterationGenerator<Double, Double, P>() {

    abstract fun calculate(parameter: P, value: Double): Double

    private tailrec fun iterate(parameter: P, interactions: Int, interaction: Int, priorValue: Double, values: List<Double>): List<Double> {
        val value = calculate(parameter, priorValue)
        notify(RunningEvent(interaction, priorValue, value))

        return if (interaction == interactions) values + value
        else iterate(parameter, interactions, interaction + 1, value, values + value)
    }

    override fun run(parameter: P, interactions: Int, initialValue: Double): List<Double> =
            iterate(parameter, interactions, 1, initialValue, listOf(initialValue))

}

abstract class IterationGeneratorBi<P : IterationParameter> : IterationGenerator<Point2D, Point2D, P>() {

    abstract fun calculate(parameter: P, value: Point2D): Point2D

    private tailrec fun iterate(parameter: P, interactions: Int, interaction: Int, priorValue: Point2D, values: List<Point2D>): List<Point2D> {
        val value = calculate(parameter, priorValue)
        notify(RunningEvent(interaction, priorValue, value))

        return if (interaction == interactions) values + value
        else iterate(parameter, interactions, interaction + 1, value, values + value)
    }

    override fun run(parameter: P, interactions: Int, initialValue: Point2D): List<Point2D> =
            iterate(parameter, interactions, 1, initialValue, listOf(initialValue))

}
