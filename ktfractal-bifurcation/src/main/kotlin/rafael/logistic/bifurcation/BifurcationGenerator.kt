package rafael.logistic.bifurcation

import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.generation.IterationParameter
import java.util.stream.Collectors

interface IBifurcationParameter : IterationParameter {
    val iterationsPerR: Int
    val stepsForR: Int
    val rMin: Double
    val rMax: Double
    val firstIteration: Int

    val rStep
        get() = (rMax - rMin) / stepsForR
}

data class BifurcationParameter(
    override val iterationsPerR: Int,
    override val stepsForR: Int,
    override val rMin: Double,
    override val rMax: Double,
    override val firstIteration: Int
) : IBifurcationParameter

abstract class BifurcationGenerator<P : IBifurcationParameter> : IterationGenerator<Double, RData, P> {

    private tailrec fun calculate(
        col: Int,
        previousValue: Double,
        r: Double,
        sequenceSkipper: (DoubleArray) -> DoubleArray,
        parameter: P,
        i: Int,
        values: DoubleArray
    ): RData {
        if (i >= values.size) {
            return RData(col, r, sequenceSkipper(values))
        }

        val currentValue = getNextX(r, previousValue, parameter)
        values[i] = currentValue
        return calculate(
            col, currentValue,
            r, sequenceSkipper, parameter, i + 1, values
        )
    }

    private fun calculateForCol(
        col: Int,
        parameter: P,
        x0: Double,
        sequenceSkipper: (DoubleArray) -> DoubleArray
    ): RData {
        val r = col * parameter.rStep + parameter.rMin
        val values = DoubleArray(parameter.iterationsPerR)
        initValues(values, x0, parameter)
        val initPos = initPosSequence()

        return calculate(col, x0, r, sequenceSkipper, parameter, initPos, values)
    }

    protected open fun initValues(sequence: DoubleArray, x0: Double, parameter: P) {
        sequence[0] = x0
    }

    protected open fun initPosSequence(): Int = 1

    protected abstract fun getNextX(r: Double, x: Double, parameter: P): Double

    override fun generate(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") x0: Double,
        parameter: P,
        interactions: Int
    ): List<RData> {
        val sequenceSkipper: (DoubleArray) -> DoubleArray =
            if (parameter.firstIteration == 0) { s -> s }
            else { s -> s.copyOfRange(parameter.firstIteration, s.size) }

        return (0..parameter.stepsForR)
            .toList().parallelStream()
            .map { col -> calculateForCol(col, parameter, x0, sequenceSkipper) }
            .collect(Collectors.toList())
    }

}

interface IBifurcationParameterWithPrior : IBifurcationParameter {
    val xMinus1: Double

    var xPrior: Double
}

open class BifurcationParameterWithPrior(
    override val iterationsPerR: Int,
    override val stepsForR: Int,
    override val rMin: Double,
    override val rMax: Double,
    override val firstIteration: Int,
    override val xMinus1: Double
) : IBifurcationParameterWithPrior {
    @Suppress("LeakingThis")
    override var xPrior = xMinus1
}

abstract class BifurcationGeneratorWithPrior<P : IBifurcationParameterWithPrior> : BifurcationGenerator<P>() {

    override fun initValues(sequence: DoubleArray, x0: Double, parameter: P) {
        sequence[0] = parameter.xMinus1
        sequence[1] = x0

        parameter.xPrior = parameter.xMinus1
    }

    override fun initPosSequence(): Int = 2

    override fun getNextX(r: Double, x: Double, parameter: P): Double {
        val nextX = getNextXWithPrior(r, x, parameter.xPrior, parameter)

        parameter.xPrior = x

        return nextX
    }

    protected abstract fun getNextXWithPrior(r: Double, x: Double, xPrior: Double, parameter: P): Double

}
