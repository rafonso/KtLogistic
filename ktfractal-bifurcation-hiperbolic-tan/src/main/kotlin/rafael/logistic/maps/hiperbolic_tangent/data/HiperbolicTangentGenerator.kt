package rafael.logistic.maps.hiperbolic_tangent.data

import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.generation.IterationParameter
import java.util.stream.Collectors
import kotlin.math.tanh

data class HiperbolicTangentParameter(
    val iterationsPerG: Int,
    val stepsForG: Int,
    val gMin: Double,
    val gStep: Double,
    val percentToSkip: Int
) :
    IterationParameter

// https://en.wikipedia.org/wiki/Logistic_map#Feigenbaum_universality_of_1-D_maps
class HiperbolicTangentGenerator : IterationGenerator<Double, GData, HiperbolicTangentParameter> {

    private tailrec fun calculate(
        col: Int,
        previousValue: Double,
        g: Double,
        maxIterations: Int,
        sequenceSkipper: (Data) -> Data,
        sequenceForR: MutableList<Double>
    ): GData {
        if (sequenceForR.size == maxIterations) {
            return GData(col, g, sequenceSkipper(sequenceForR))
        }

        val currentValue = g * previousValue * (1.0 - tanh(previousValue))
        sequenceForR.add(currentValue)
        return calculate(col, currentValue, g, maxIterations, sequenceSkipper, sequenceForR)
    }

    override fun generate(x0: Double, parameter: HiperbolicTangentParameter, interactions: Int): List<GData> {
        val sequenceSkipper: (List<Double>) -> List<Double> = if (parameter.percentToSkip == 0) { s -> s }
        else { s -> s.subList((s.size * parameter.percentToSkip.toDouble() / 100).toInt(), s.size) }

        return (0..parameter.stepsForG)
            .map { step -> Pair(step, step * parameter.gStep + parameter.gMin) }
            .toList().parallelStream()
            .map { (col, g) ->
                calculate(col, x0, g, parameter.iterationsPerG, sequenceSkipper, mutableListOf(x0))
            }
            .collect(Collectors.toList())
    }


    fun generate(
        x0: Double,
        gMin: Double,
        gMax: Double,
        stepsForG: Int,
        percentToSkip: Int,
        iterationsPerG: Int
    ): List<GData> {
        val gStep = (gMax - gMin) / stepsForG

        return generate(
            x0,
            HiperbolicTangentParameter(iterationsPerG, stepsForG, gMin, gStep, percentToSkip),
            stepsForG
        )
    }


}
// : {\displaystyle x\rightarrow Gx(1-\tanh(x))}{\displaystyle x\rightarrow Gx(1-\tanh(x))}
