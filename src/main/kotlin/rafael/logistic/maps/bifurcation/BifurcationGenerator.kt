package rafael.logistic.maps.bifurcation

import rafael.logistic.generator.IterationGenerator
import rafael.logistic.generator.IterationParameter
import java.util.stream.Collectors

data class BifurcationParameter(val iterationsPerR: Int, val stepsForR: Int, val rStep: Double, val percentToSkip: Int) : IterationParameter

class BifurcationGenerator : IterationGenerator<RData, BifurcationParameter>() {

    override fun calculate(parameter: BifurcationParameter, value: RData): RData {
        println("\t$value")
        return value
    }

    private tailrec fun calculate(previousValue: Double, r: Double, maxIterations: Int,
                                  sequenceSkipper: (List<Double>) -> List<Double>, sequenceForR: List<Double>): RData {
        if (sequenceForR.size == maxIterations) {
            return RData(r, sequenceSkipper(sequenceForR))
        }

        val currentValue = r * previousValue * (1.0 - previousValue)
        return calculate(currentValue, r, maxIterations, sequenceSkipper, sequenceForR + currentValue)
    }

    override fun run(parameter: BifurcationParameter, interactions: Int, initialValue: RData): List<RData> {
        val x0 = initialValue.values[0]
        val sequenceSkipper: (List<Double>) -> List<Double> = if (parameter.percentToSkip == 0) { s -> s }
        else { s -> s.subList((s.size * parameter.percentToSkip.toDouble() / 100).toInt(), s.size) }

        return (0..parameter.stepsForR)
                .map { step -> step * parameter.rStep }
                .toList().parallelStream()
                .map { r -> calculate(x0, r, parameter.iterationsPerR, sequenceSkipper, listOf(x0)) }
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

