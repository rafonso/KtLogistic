package rafael.logistic.view.mapchart

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
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

    private val square = Point0()

    init {
        myXAxis.tickLabelFormatter = CONVERTER_2
        myYAxis.tickLabelFormatter = CONVERTER_2
        dataProperty.onChange {
            layoutPlotChildren()
        }
        initialize()
    }

    protected fun highlightP0(x0: Double, y0: Double) {
        background.add(square.also { sq ->
            sq.x = x0.realToChartX()
            sq.y = y0.realToChartY()
            sq.toFront()

//            sq.addEventHandler(MouseEvent.MOUSE_DRAGGED) { event ->
//                val pos = background.sceneToLocal(event.sceneX, event.sceneY)
//                p0Moved(sq, pos)
//                println("(${sq.x}, ${sq.y})\t ${pos}\t ")
//            }
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
    protected fun Double.chartToRealX(): Number = myXAxis.getValueForDisplay(this)!!

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
    protected fun Double.chartToRealY(): Number = myYAxis.getValueForDisplay(this)!!



//    protected fun Point0.toPoint2D() = Point2D(
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
