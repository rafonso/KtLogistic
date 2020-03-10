package rafael.logistic.view.mapchart

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.input.MouseEvent
import rafael.logistic.view.CONVERTER_2
import tornadofx.*

const val P0_SIDE = 20.0

abstract class MapChartBase<T>(
        xAxis: Axis<Double>,
        yAxis: Axis<Double>,
        data: ObservableList<Series<Double, Double>>) : LineChart<Double, Double>(xAxis, yAxis, data) {

    constructor(xAxis: Axis<Double>, yAxis: Axis<Double>) :
            this(xAxis, yAxis, mutableListOf<Series<Double, Double>>().observable())

    protected val background: Node = super.lookup(".chart-plot-background")

    private val dataProperty = emptyList<T>().toProperty()
    protected var data: List<T> by dataProperty

    protected val myXAxis = (xAxis as NumberAxis)

    protected val myYAxis = (yAxis as NumberAxis)

    val square = Point0().apply {
        xChartToReal = myXAxis::getValueForDisplay as (Double) -> Double
        yChartToReal = myYAxis::getValueForDisplay as (Double) -> Double
    }

    init {
        super.prefWidthProperty().bindBidirectional(super.prefHeightProperty())
        myXAxis.tickLabelFormatter = CONVERTER_2
        myYAxis.tickLabelFormatter = CONVERTER_2
        dataProperty.onChange {
            layoutPlotChildren()
        }
        initialize()


//        square.xRealProperty().onChange { println("(${square.xChart}, ${square.yChart})\t(${square.xRealProperty().value}, ${square.yRealProperty().value})") }
//        square.yRealProperty().onChange { println("(${square.xChart}, ${square.yChart})\t(${square.xRealProperty().value}, ${square.yRealProperty().value})") }

    }

    protected fun highlightP0(x0: Double, y0: Double) {
        background.add(square.also { sq ->
            sq.xChart = x0.realToChartX()
            sq.yChart = y0.realToChartY()
            sq.toFront()

            sq.addEventHandler(MouseEvent.MOUSE_DRAGGED) { event ->
                val pos = background.sceneToLocal(event.sceneX, event.sceneY)
                sq.xChart = pos.x
                sq.yChart = pos.y
//
//                val x = sq.xRealProperty().value
//                val y = sq.yRealProperty().value
////                p0Moved(sq, pos)
//                println("(${sq.xChart}, ${sq.yChart})\t ($x, $y)\t ")
            }
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
    protected fun Double.chartToRealX() = myXAxis.getValueForDisplay(this)

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
    protected fun Double.chartToRealY() = myYAxis.getValueForDisplay(this)


//    protected fun Point0.toBiPoint() = BiPoint(
//            myXAxis.getValueForDisplay(this.x) as Double,
//            myYAxis.getValueForDisplay(this.y) as Double
//    )

    protected abstract fun plotData()

//    protected abstract fun p0Moved(square: Point0, pos: Point2D)

    protected open fun initialize() {

    }

    fun bind(dataProperty: ReadOnlyObjectProperty<List<T>>, handler: (MapChartBase<T>) -> Unit = {}) {
        this.dataProperty.bind(dataProperty)
        handler(this)
    }

    override fun layoutPlotChildren() {
        background.getChildList()?.clear()
        plotData()
    }

}
