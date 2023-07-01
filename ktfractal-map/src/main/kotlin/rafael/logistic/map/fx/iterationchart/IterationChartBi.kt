package rafael.logistic.map.fx.iterationchart

import javafx.beans.NamedArg
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.scene.chart.Axis
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.PathElement
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.GenerationStatus
import tornadofx.*

class IterationChartBi(
    @NamedArg("xAxis") xAxis: Axis<Int>,
    @NamedArg("yAxis") yAxis: Axis<Double>,
    @NamedArg("data") data: ObservableList<Series<Int, Double>>
) : IterationChartBase<BiDouble>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Int>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Int, Double>>().asObservable<Series<Int, Double>>())

    companion object {
        internal val extractorX: (BiDouble) -> Double = BiDouble::x
        internal val extractorY: (BiDouble) -> Double = BiDouble::y
    }

    private lateinit var extractor: (BiDouble) -> Double

    /**
     * Vincula a atualização do gráfico a quando o status do gráfico principal chegar
     * ao [status][GenerationStatus] [GenerationStatus.PLOTTING_FINALIZING].
     *
     * @param statusProperty properiedade do status do gráfico principal
     * @param iterationsProperty Propriedade relacionada a quantidade de iterações
     * @param dataProperty Propriedade relacionada aos dados brutos.
     * @param function Função responsável por extrair o valor apropriado de [BiDouble].
     */
    fun bind(
        statusProperty: ObservableValue<GenerationStatus>,
        iterationsProperty: ObservableValue<Int>,
        dataProperty: ObservableValue<List<BiDouble>>,
        function: (BiDouble) -> Double
    ) {
        super.bind(statusProperty, iterationsProperty, dataProperty)
        this.extractor = function
    }

    override fun loadPath(iterationData: List<BiDouble>): Array<PathElement> {
        val positions: List<Pair<Int, Double>> = iterationData
            .mapIndexed { i, pt -> Pair(i, extractor(pt)) }
            .filter { pair -> pair.second >= valueYAxis.lowerBound && pair.second <= valueYAxis.upperBound }

        return positions.indices
            .map { index ->
                val moveCursor = (index == 0) || (positions[index].first - positions[index - 1].first > 1)
                val xPos = positions[index].first.toIterationsXPos()
                val yPos = positions[index].second.toIterationsYPos()

                Triple(moveCursor, xPos, yPos)
            }
            .map { t -> if (t.first) MoveTo(t.second, t.third) else LineTo(t.second, t.third) }
            .toTypedArray()
    }

}
