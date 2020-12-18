@file:Suppress("LeakingThis")

package rafael.logistic.map.fx.mapchart

import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import rafael.logistic.core.fx.CONVERTER_2
import rafael.logistic.core.fx.mapchart.MapChart
import rafael.logistic.core.fx.mapchart.exportImageTo
import rafael.logistic.core.fx.zeroProperty
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.GenerationStatus
import tornadofx.*
import java.io.File

const val P0_SIDE = 20.0

abstract class MapChartBase<T>(
    xAxis: Axis<Double>,
    yAxis: Axis<Double>,
    data: ObservableList<Series<Double, Double>>
) : LineChart<Double, Double>(xAxis, yAxis, data), MapChart<T, Array<Node>> {

    // @formatter:off

    protected           val background                  :   Node = super.lookup(".chart-plot-background")

    private             val dataProperty                =   emptyList<T>().toProperty()
    override            val data0Property               =   dataProperty

    protected           val myXAxis                     =   (xAxis as NumberAxis)

    protected           val myYAxis                     =   (yAxis as NumberAxis)

    private             val square                      =   Point0()

    private             val mousePositionRealProperty   =   BiDouble(0.0, 0.0).toProperty()

    final   override    val xMinProperty                =   zeroProperty()
            override    val xMin                        by  xMinProperty

    final   override    val xMaxProperty                =   zeroProperty()
            override    val xMax                        by  xMaxProperty

    final   override    val yMinProperty                =   zeroProperty()
            override    val yMin                        by  yMinProperty

    final   override    val yMaxProperty                =   zeroProperty()
            override    val yMax                        by  yMaxProperty

    private             val deltaXByPixelProp           =   zeroProperty()
            override    val deltaXByPixelProperty       =   deltaXByPixelProp as ReadOnlyDoubleProperty

    private             val deltaYByPixelProp           =   zeroProperty()
            override    val deltaYByPixelProperty       =   deltaYByPixelProp as ReadOnlyDoubleProperty

            override    val generationStatusProperty    =   GenerationStatus.IDLE.toProperty()

    private lateinit    var dataGenerator               :   (() -> List<T>)

    // @formatter:on

    init {
        xMinProperty.bindBidirectional(myXAxis.lowerBoundProperty())
        xMaxProperty.bindBidirectional(myXAxis.upperBoundProperty())
        myXAxis.tickLabelFormatter = CONVERTER_2
        deltaXByPixelProp.bind((xMaxProperty - xMinProperty) / myXAxis.widthProperty())

        yMinProperty.bindBidirectional(myYAxis.lowerBoundProperty())
        yMaxProperty.bindBidirectional(myYAxis.upperBoundProperty())
        myYAxis.tickLabelFormatter = CONVERTER_2
        deltaYByPixelProp.bind((yMaxProperty - yMinProperty) / myYAxis.heightProperty())

        background.onMouseMoved = EventHandler { event ->
            mousePositionRealProperty.value = BiDouble(event.x.chartToRealX(), event.y.chartToRealY())
        }
        background.onMouseExited = EventHandler { event ->
            mousePositionRealProperty.value = BiDouble(event.x.chartToRealX(), event.y.chartToRealY())
        }

        initialize()
    }

    protected fun highlightP0(x0: Double, y0: Double) {
        background.add(square.also { sq ->
            sq.x = x0.realToChartX()
            sq.y = y0.realToChartY()
            sq.toFront()
        })
    }

    /**
     * Converte um valor real para seu equivalente no eixo X do [LineChart].
     *
     * @see Axis#getDisplayPosition()
     */
    protected fun Double.realToChartX() = myXAxis.getDisplayPosition(this)

    /**
     * Converte um valor no eixo X do [LineChart] para seu equivalente real.
     *
     * @see Axis#getValueForDisplay()
     */
    private fun Double.chartToRealX(): Double = myXAxis.getValueForDisplay(this).toDouble()

    /**
     * Converte um valor real para seu equivalente no eixo Y do [LineChart].
     *
     * @see Axis#getDisplayPosition
     */
    protected fun Double.realToChartY() = myYAxis.getDisplayPosition(this)

    /**
     * Converte um valor no eixo Y do [LineChart] para seu equivalente real.
     *
     * @see Axis#getValueForDisplay()
     */
    private fun Double.chartToRealY(): Double = myYAxis.getValueForDisplay(this).toDouble()

    protected open fun initialize() {

    }

    override fun layoutPlotChildren() {
        refreshData(false)
    }

    override fun bind(dataGenerator: () -> List<T>) {
        this.dataGenerator = dataGenerator
    }

    override fun mousePositionRealProperty() = mousePositionRealProperty as ReadOnlyObjectProperty<BiDouble>

    override fun exportImageTo(file: File): Boolean =
        exportImageTo(this, super.getWidth().toInt(), super.getHeight().toInt(), file)

    override fun reloadData() {
        this.dataProperty.value = this.dataGenerator()
    }

}
