package rafael.logistic.view

import javafx.beans.NamedArg
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import rafael.logistic.generator.BiPoint
import tornadofx.*

const val P0_SIDE = 10.0

class MapChartBi(
        @NamedArg("xAxis") xAxis: Axis<Double>,
        @NamedArg("yAxis") yAxis: Axis<Double>,
        @NamedArg("data") data: ObservableList<Series<Double, Double>>) : LineChart<Double, Double>(xAxis, yAxis, data) {

    constructor(@NamedArg("xAxis") xAxis: Axis<Double>, @NamedArg("yAxis") yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    private val background: Node = super.lookup(".chart-plot-background")

    val dataProperty = emptyList<BiPoint>().toProperty()
    private var data: List<BiPoint> by dataProperty

    private val myXAxis = (xAxis as NumberAxis)

    private val myYAxis = (yAxis as NumberAxis)

    init {
        myXAxis.tickLabelFormatter = CONVERTER_2
        myYAxis.tickLabelFormatter = CONVERTER_2
        background.style {
            backgroundColor += c("white")
        }
        dataProperty.onChange {
            layoutPlotChildren()
        }
    }

    private fun Double.toLogisticXPos() = myXAxis.getDisplayPosition(this)

    private fun Double.toLogisticYPos() = myYAxis.getDisplayPosition(this)

    private fun highlightP0(p0: BiPoint) {
        val cornerX = p0.x.toLogisticXPos() - P0_SIDE / 2
        val cornerY = p0.y.toLogisticYPos() - P0_SIDE / 2
        background.add(
                Path(
                        // @formatter:off
                        MoveTo(cornerX              , cornerY           ),
                        LineTo(cornerX + P0_SIDE    , cornerY           ),
                        LineTo(cornerX + P0_SIDE    , cornerY + P0_SIDE ),
                        LineTo(cornerX              , cornerY + P0_SIDE ),
                        LineTo(cornerX              , cornerY           ),
                        LineTo(cornerX + P0_SIDE    , cornerY + P0_SIDE ),
                        MoveTo(cornerX              , cornerY + P0_SIDE ),
                        LineTo(cornerX + P0_SIDE    , cornerY           )
                        // @formatter:on
                ).apply {
                    fill = Color.TRANSPARENT
                    stroke = Color.DARKGRAY
                }
        )
    }

    private fun refreshData() {
        data
                .filter { p ->
                    ((p.x >= myXAxis.lowerBound) || (p.x <= myXAxis.upperBound)) &&
                            ((p.y >= myYAxis.lowerBound) || (p.y <= myYAxis.upperBound))
                }
                .map { p -> Pair(p.x.toLogisticXPos(), p.y.toLogisticYPos()) }
                .mapIndexed { index, pair ->
                    circle(pair.first, pair.second, (1.6 * index / data.size + 0.4)).apply {
                        stroke = getStroke(index.toDouble() / data.size)
                        fill = stroke
                    }
                }
                .forEach { l -> background.add(l) }
    }

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()

        if (data.isNotEmpty()) {
            // Destaca o x0 e y0
            highlightP0(data.first())

            refreshData()
        }
    }

}