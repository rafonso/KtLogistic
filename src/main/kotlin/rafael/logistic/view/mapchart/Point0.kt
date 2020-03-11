package rafael.logistic.view.mapchart

import javafx.beans.InvalidationListener
import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.beans.property.adapter.JavaBeanDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.beans.value.ObservableValueBase
import javafx.beans.value.WritableDoubleValue
import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Callback
import tornadofx.*
import java.util.concurrent.Callable

private const val SIDE = 20.0

typealias DoubleConverter = (Double) -> Double

class Point0(private val xChartToReal: DoubleConverter, private val xRealToChart: DoubleConverter,
             private val yChartToReal: DoubleConverter, private val yRealToChart: DoubleConverter) : Path(
        // @formatter:off
        MoveTo(0.0  , 0.0   ),
        LineTo(SIDE , 0.0   ),
        LineTo(SIDE , SIDE  ),
        LineTo(0.0  , SIDE  ),
        LineTo(0.0  , 0.0   ),
        LineTo(SIDE , SIDE  ),
        MoveTo(0.0  , SIDE  ),
        LineTo(SIDE , 0.0   )
        // @formatter:on
) {

    val xChartProperty = SimpleDoubleProperty(this, "xChart", 0.0)
    var xChart by xChartProperty

    val xRealProperty = ChartToRealProperty("xReal", xChartProperty, xChartToReal, xRealToChart)
    var xReal by xRealProperty

    val yChartProperty = SimpleDoubleProperty(this, "yChart", 0.0)
    var yChart by yChartProperty

    val yRealProperty = ChartToRealProperty("yReal", yChartProperty, yChartToReal, yRealToChart)//  (0.0).toProperty()
    var yReal by yRealProperty

    init {


        this.xChartProperty.addListener { _, _, newX ->
            translateX = newX.toDouble() - SIDE / 2
            xRealProperty.invalidated()
        }
//        xRealProperty.bind(Bindings.createDoubleBinding(Callable { xChartToReal(xChartProperty.value) }, xChartProperty))
        this.yChartProperty.addListener { _, _, newY ->
            translateY = newY.toDouble() - SIDE / 2
            yRealProperty.invalidated()
        }

        fill = Color.TRANSPARENT
        stroke = Color.DARKGRAY
        super.addEventHandler(MouseEvent.MOUSE_PRESSED) { cursor = Cursor.CROSSHAIR }
        super.addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            stroke = Color.BLACK
            cursor = Cursor.CROSSHAIR
        }
        onMouseReleased = EventHandler {
            cursor = Cursor.DEFAULT
            stroke = Color.DARKGREY
        }
        onMouseEntered = EventHandler {
            stroke = Color.BLACK
        }
        onMouseExited = EventHandler {
            stroke = Color.DARKGREY
            cursor = Cursor.DEFAULT
        }

    }


}


class ChartToRealProperty(
        private val name: String,
        private val chartProperty: DoubleProperty,
        private val chartToReal: DoubleConverter,
        private val realToChart: DoubleConverter) : DoublePropertyBase(chartToReal(chartProperty.value)) {


    override fun getName(): String = name

    override fun getBean(): Any = chartProperty.bean

    override fun get(): Double = chartToReal(chartProperty.get())

    override fun set(newValue: Double) {
        chartProperty.value = realToChart(newValue)
        super.fireValueChangedEvent()
    }

    public override fun invalidated() {
        super.fireValueChangedEvent()
    }

}