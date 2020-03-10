package rafael.logistic.view.mapchart

import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyDoubleProperty
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

class Point0 : Path(
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

    internal lateinit var xChartToReal: (Double) -> Double

    internal lateinit var yChartToReal: (Double) -> Double

    private val xChartProperty = (0.0).toProperty()
    var xChart by xChartProperty

    private val xRealProperty = (0.0).toProperty()
    fun xRealProperty() = xRealProperty as ReadOnlyDoubleProperty

    private val yChartProperty = (0.0).toProperty()
    var yChart by yChartProperty

    private val yRealProperty = (0.0).toProperty()
    fun yRealProperty() = yRealProperty as ReadOnlyDoubleProperty

    init {
        this.xChartProperty.addListener { _, _, newX ->
            translateX = newX.toDouble() - P0_SIDE / 2
            xRealProperty.value = xChartToReal(newX.toDouble())
        }
//        xRealProperty.bind(Bindings.createDoubleBinding(Callable { xChartToReal(xChartProperty.value) }, xChartProperty))
        this.yChartProperty.addListener { _, _, newY ->
            translateY = newY.toDouble() - P0_SIDE / 2
            yRealProperty.value = yChartToReal(newY.toDouble())
        }
//        yRealProperty.bind(Bindings.createDoubleBinding(Callable { yChartToReal(yChartProperty.value) }, yChartProperty))

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
