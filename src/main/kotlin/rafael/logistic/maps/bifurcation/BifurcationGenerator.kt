package rafael.logistic.maps.bifurcation

import rafael.logistic.generator.IterationGenerator
import rafael.logistic.generator.IterationParameter
import java.util.stream.Collectors

data class BifurcationParameter(val iterationsPerR: Int, val stepsForR: Int, val rStep: Double, val percentToSkip: Int) : IterationParameter

class BifurcationGenerator : IterationGenerator<RData, BifurcationParameter>() {

    private data class CalculateParameter(val r: Double, val maxIterations: Int, val sequenceSkipper: (Data) -> Data) {
        val convergenceType = ConvergenceType.valueOf(r)

        val verifier = when (convergenceType) {
            ConvergenceType.ZERO     -> ZeroVerifier
            ConvergenceType.CONSTANT -> ConstanstVerifier(r)
            ConvergenceType.CYCLE_2  -> Cycle2Verifier
            ConvergenceType.CHAOS    -> ChaosVerifier
        }
    }

    override fun calculate(parameter: BifurcationParameter, value: RData): RData {
        println("\t$value")
        return value
    }

    private tailrec fun calculate(previousValue: Double, parameter: CalculateParameter, sequenceForR: Data): RData {
        if (sequenceForR.size == parameter.maxIterations || parameter.verifier.converges(sequenceForR)) {
            return RData(parameter.r, parameter.sequenceSkipper(sequenceForR), parameter.convergenceType)
        }

        val currentValue = parameter.r * previousValue * (1.0 - previousValue)
        return calculate(currentValue, parameter, sequenceForR + currentValue)
    }

    override fun run(parameter: BifurcationParameter, interactions: Int, initialValue: RData): List<RData> {
        val x0 = initialValue.values[0]
        val sequenceSkipper: (List<Double>) -> List<Double> = if (parameter.percentToSkip == 0) { s -> s }
        else { s -> s.subList((s.size * parameter.percentToSkip.toDouble() / 100).toInt(), s.size) }

        return (0..parameter.stepsForR)
                .map { step -> step * parameter.rStep }
                .toList().parallelStream()
                .map { r -> calculate(x0, CalculateParameter(r, parameter.iterationsPerR, sequenceSkipper), listOf(x0)) }
                .collect(Collectors.toList())
    }

    fun generate(x0: Double, rMin: Double, rMax: Double, stepsForR: Int, percentToSkip: Int, iterationsPerR: Int): List<RData> {
        val rStep = (rMax - rMin) / stepsForR

        return super.generate(RData(0.0, x0), BifurcationParameter(iterationsPerR, stepsForR, rStep, percentToSkip), stepsForR).also {
            println(it.size)
            println("-".repeat(30))
        }
    }

}

