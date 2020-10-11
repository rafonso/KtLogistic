package rafael.logistic.maps.bifurcation

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
        col: Int, previousValue: Double,
        r: Double,
        maxIterations: Int,
        sequenceSkipper: (DoubleArray) -> DoubleArray,
        i: Int,
        sequenceForR: DoubleArray
    ): RData {
        if (i == maxIterations) {
            return RData(col, r, sequenceSkipper(sequenceForR))
        }

        val currentValue = getNextX(r, previousValue)
        sequenceForR[i] = currentValue
        return calculate(
            col, currentValue,
            r, maxIterations, sequenceSkipper, i + 1, sequenceForR
        )
    }

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

                calculate(
                    col,
                    x0,
                    r,
                    parameter.iterationsPerR,
                    sequenceSkipper,
                    1,
                    DoubleArray(parameter.iterationsPerR).also { it[0] = x0 })
            }
            .collect(Collectors.toList())
    }


    fun generate(
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

