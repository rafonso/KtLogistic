package rafael.logistic.maps.bifurcation

import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.generation.IterationParameter
import java.util.stream.Collectors

data class BifurcationParameter(val iterationsPerR: Int, val stepsForR: Int, val rMin: Double, val rStep: Double, val percentToSkip: Int) : IterationParameter

class BifurcationGenerator : IterationGenerator<Double, RData, BifurcationParameter> {

    private tailrec fun calculate(col: Int, previousValue: Double,
                                  convergenceType: ConvergenceType,
                                  verifier: ConvergenceVerifier,
                                  r: Double,
                                  maxIterations: Int,
                                  sequenceSkipper: (Data) -> Data,
                                  sequenceForR: MutableList<Double>): RData {
        if (sequenceForR.size == maxIterations || verifier.converges(sequenceForR)) {
            return RData(col, r, sequenceSkipper(sequenceForR), convergenceType)
        }

        val currentValue = r * previousValue * (1.0 - previousValue)
        sequenceForR.add(currentValue)
        return calculate(col, currentValue, convergenceType, verifier, r, maxIterations, sequenceSkipper, sequenceForR)
    }


    override fun generate(x0: Double, parameter: BifurcationParameter, interactions: Int): List<RData> {
        val sequenceSkipper: (List<Double>) -> List<Double> = if (parameter.percentToSkip == 0) { s -> s }
        else { s -> s.subList((s.size * parameter.percentToSkip.toDouble() / 100).toInt(), s.size) }

        return (0..parameter.stepsForR)
                .map { step -> Pair(step, step * parameter.rStep + parameter.rMin) }
                .toList().parallelStream()
                .map { (col, r) ->
                    val convergenceType = ConvergenceType.valueOf(r)
                    val verifier = ConvergenceVerifier.valueOf(convergenceType, r)

                    calculate(col, x0, convergenceType, verifier, r, parameter.iterationsPerR, sequenceSkipper, mutableListOf(x0))
                }
                .collect(Collectors.toList())
    }


    fun generate(x0: Double, rMin: Double, rMax: Double, stepsForR: Int, percentToSkip: Int, iterationsPerR: Int): List<RData> {
        val rStep = (rMax - rMin) / stepsForR

        return generate(x0, BifurcationParameter(iterationsPerR, stepsForR, rMin, rStep, percentToSkip), stepsForR)
    }

}

