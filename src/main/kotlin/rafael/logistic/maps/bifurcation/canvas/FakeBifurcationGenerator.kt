package rafael.logistic.maps.bifurcation.canvas

import rafael.logistic.maps.bifurcation.BifurcationParameter
import rafael.logistic.maps.bifurcation.ConvergenceType
import rafael.logistic.maps.bifurcation.RData
import rafael.logistic.view.IterationGenerator
import java.util.stream.Collectors

class FakeBifurcationGenerator : IterationGenerator<Double, RData, BifurcationParameter> {

    override fun generate(initialValue: Double, parameter: BifurcationParameter, interactions: Int): List<RData> {

        return (0..parameter.stepsForR)
                .map { step -> step * parameter.rStep + parameter.rMin }
                .toList().parallelStream()
                .map { r -> RData(r, listOf(r / 4, 1 - r / 4), ConvergenceType.CYCLE_2) }
                .collect(Collectors.toList())
    }


    fun generate(x0: Double, rMin: Double, rMax: Double, stepsForR: Int, percentToSkip: Int, iterationsPerR: Int): List<RData> {
        val rStep = (rMax - rMin) / stepsForR

        return generate(x0, BifurcationParameter(iterationsPerR, stepsForR, rMin, rStep, percentToSkip), stepsForR)
    }

}