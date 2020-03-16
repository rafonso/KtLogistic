package rafael.logistic.maps.baker

import javafx.geometry.Point2D
import rafael.logistic.view.IterationGeneratorBi
import rafael.logistic.view.NoParameter

class BakerGenerator : IterationGeneratorBi<NoParameter>() {

    companion object {
        fun calc(p: Point2D) =
                when (p.x) {
                    in 0.0..0.5 -> Point2D(+ 2 * p.x    , + p.y / 2)
                    in 0.5..1.0 -> Point2D(- 2 * p.x + 2, - p.y / 2 + 1.0)
                    else        -> error("$p")
                }
    }

    override fun calculate(parameter: NoParameter, point: Point2D): Point2D =
            calc(point)

    fun generate(p0: Point2D, iterations: Int) =
            super.generate(p0, NoParameter, iterations)

}
