package rafael.logistic.maps.bifurcation

import javafx.geometry.Point2D
import rafael.logistic.generator.IterationGenerator
import rafael.logistic.generator.IterationParameter
import java.util.stream.Collectors

data class BifurcationParameter(val iterationsPerR: Int) : IterationParameter

class BifurcationGenerator : IterationGenerator<List<Point2D>, BifurcationParameter>() {


    override fun calculate(parameter: BifurcationParameter, value: List<Point2D>): List<Point2D> {
        println("\t$value")
        return value
    }

    private tailrec fun calculate(previousPoint: Point2D, maxIterations: Int, sequenceForR: List<Point2D>): List<Point2D> {
        if (sequenceForR.size == maxIterations) {
            return sequenceForR
        }

        val currentPoint = Point2D(previousPoint.x, previousPoint.x * previousPoint.y * (1.0 - previousPoint.y))
        return calculate(currentPoint, maxIterations, sequenceForR + currentPoint)
    }

    override fun run(parameter: BifurcationParameter, interactions: Int, initialValue: List<Point2D>): List<List<Point2D>> {
//        println("\t$initialValue")
        return initialValue
                .parallelStream()
                .map { rInitialPoint -> calculate(rInitialPoint, parameter.iterationsPerR, listOf(rInitialPoint)) }
                .collect(Collectors.toList())
    }


    fun generate(x0: Double, rMin: Double, rMax: Double, stepsForR: Int, iterationsPerR: Int): List<List<Point2D>> {
        val rStep = (rMax - rMin) / stepsForR
        val rs = (0..stepsForR).map { step -> step * rStep }.map { r -> Point2D(r, x0) }

        return super.generate(rs, BifurcationParameter(iterationsPerR), stepsForR).also {
            println(it.size)
            println("-".repeat(30))
        }
    }

}
