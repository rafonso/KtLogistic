package rafael.logistic.view.mapchart

import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import rafael.logistic.view.CONVERTER_2
import rafael.logistic.view.GenerationStatus
import tornadofx.*

const val P0_SIDE = 20.0

abstract class MapChartBase<T>(
        xAxis: Axis<Double>,
        yAxis: Axis<Double>,
        data: ObservableList<Series<Double, Double>>) : LineChart<Double, Double>(xAxis, yAxis, data), MapChart<T, Node> {

    constructor(xAxis: Axis<Double>, yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    // @formatter:off

    protected       val background: Node            =   super.lookup(".chart-plot-background")

    private         val dataProperty                =   emptyList<T>().toProperty()
    protected       var data: List<T>               by  dataProperty

    protected       val myXAxis                     =   (xAxis as NumberAxis)

    protected       val myYAxis                     =   (yAxis as NumberAxis)

    private         val square                      =   Point0()

    private         val mousePositionRealProperty   =   Point2D(0.0, 0.0).toProperty()

    final override  val xMinProperty                =   (0.0).toProperty()
    override        val xMin                        by  xMinProperty

    final override  val xMaxProperty                =   (0.0).toProperty()
    override        val xMax                        by  xMaxProperty

    final override  val yMinProperty                =   (0.0).toProperty()
    override        val yMin                        by  yMinProperty

    final override  val yMaxProperty                =   (0.0).toProperty()
    override        val yMax                        by  yMaxProperty

    private         val deltaXByPixelProp           =   (0.0).toProperty()
    override        val deltaXByPixelProperty       =   deltaXByPixelProp as ReadOnlyDoubleProperty

    private         val deltaYByPixelProp           =   (0.0).toProperty()
    override        val deltaYByPixelProperty       =   deltaYByPixelProp as ReadOnlyDoubleProperty

    override        val generationStatusProperty    =   GenerationStatus.IDLE.toProperty()

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

        dataProperty.onChange {
            layoutPlotChildren()
        }

        background.onMouseMoved = EventHandler { event ->
            mousePositionRealProperty.value = Point2D(event.x.chartToRealX(), event.y.chartToRealY())
        }
        background.onMouseExited = EventHandler { event ->
            // TODO: Veficar se o mouse está dentro dos limites do backgound
            mousePositionRealProperty.value = Point2D(event.x.chartToRealX(), event.y.chartToRealY())
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
        refreshData()
    }

    override fun bind(dataProperty: ReadOnlyObjectProperty<List<T>>, handler: (MapChart<T, *>) -> Unit) {
        this.dataProperty.bind(dataProperty)
        handler(this)
    }

    override fun mousePositionRealProperty() = mousePositionRealProperty as ReadOnlyObjectProperty<Point2D>

}
