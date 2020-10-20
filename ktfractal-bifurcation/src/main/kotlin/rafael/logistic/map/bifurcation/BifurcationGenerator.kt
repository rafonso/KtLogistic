package rafael.logistic.map.bifurcation

import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.generation.IterationParameter
import java.util.stream.Collectors

data class BifurcationParameter(
    val iterationsPerR: Int,
    val stepsForR: Int,
    val rMin: Double,
    val rStep: Double,
    val percentToSkip: Int
) : IterationParameter

abstract class BifurcationGenerator : IterationGenerator<Double, RData, BifurcationParameter> {

    private tailrec fun calculate(
        col: Int,
        previousValue: Double,
        r: Double,
        sequenceSkipper: (DoubleArray) -> DoubleArray,
        i: Int,
        values: DoubleArray
    ): RData {
        if (i >= values.size) {
            return RData(col, r, sequenceSkipper(values))
        }

        val currentValue = getNextX(r, previousValue)
        values[i] = currentValue
        return calculate(
            col, currentValue,
            r, sequenceSkipper, i + 1, values
        )
    }

    protected open fun initValues(sequence: DoubleArray, x0: Double) {
        sequence[0] = x0
    }

    protected open fun initPosSequence(): Int = 1

    protected abstract fun getNextX(r: Double, x: Double): Double

    override fun generate(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") x0: Double,
        parameter: BifurcationParameter,
        interactions: Int
    ): List<RData> {
        val sequenceSkipper: (DoubleArray) -> DoubleArray = if (parameter.percentToSkip == 0) { s -> s }
        else { s -> s.copyOfRange((s.size * parameter.percentToSkip.toDouble() / 100).toInt(), s.size) }

        return (0..parameter.stepsForR)
            .toList().parallelStream()
            .map { col ->
                val r = col * parameter.rStep + parameter.rMin
                val values = DoubleArray(parameter.iterationsPerR)
                initValues(values, x0)
                val initPos = initPosSequence()

                calculate(
                    col,
                    x0,
                    r,
                    sequenceSkipper,
                    initPos,
                    values)
            }
            .collect(Collectors.toList())
    }

    open fun generate(
        x0: Double,
        rMin: Double,
        rMax: Double,
        stepsForR: Int,
        percentToSkip: Int,
        iterationsPerR: Int
    ): List<RData> {
        val rStep = (rMax - rMin) / stepsForR

        return generate(x0, BifurcationParameter(iterationsPerR, stepsForR, rMin, rStep, percentToSkip), stepsForR)
    }

}
