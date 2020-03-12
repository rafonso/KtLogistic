package rafael.logistic.maps.bifurcation

import rafael.logistic.generator.IterationGenerator
import rafael.logistic.generator.IterationParameter
import java.util.stream.Collectors

data class BifurcationParameter(val iterationsPerR: Int, val stepsForR: Int, val rStep: Double) : IterationParameter

class BifurcationGenerator : IterationGenerator<RData, BifurcationParameter>() {


    override fun calculate(parameter: BifurcationParameter, value: RData): RData {
        println("\t$value")
        return value
    }

    private tailrec fun calculate(previousValue: Double, r: Double, maxIterations: Int, sequenceForR: List<Double>): RData {
        if (sequenceForR.size == maxIterations) {
            return RData(r, sequenceForR)
        }

        val currentValue = r * previousValue * (1.0 - previousValue)
        return calculate(currentValue, r, maxIterations, sequenceForR + currentValue)
    }

    override fun run(parameter: BifurcationParameter, interactions: Int, initialValue: RData): List<RData> {
        val x0 = initialValue.values[0]

        return (0..parameter.stepsForR)
                .map { step -> step * parameter.rStep }
                .toList().parallelStream()
                .map { r -> calculate(x0, r, parameter.iterationsPerR, listOf(x0)) }
                .collect(Collectors.toList())
    }

    fun generate(x0: Double, rMin: Double, rMax: Double, stepsForR: Int, iterationsPerR: Int): List<RData> {
        val rStep = (rMax - rMin) / stepsForR

        return super.generate(RData(0.0, x0), BifurcationParameter(iterationsPerR, stepsForR, rStep), stepsForR).also {
            println(it.size)
            println("-".repeat(30))
        }
    }

}
