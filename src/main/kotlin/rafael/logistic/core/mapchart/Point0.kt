package rafael.logistic.core.mapchart

import javafx.scene.paint.Color
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import tornadofx.*

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

    private val xProperty = (0.0).toProperty()
    var x by xProperty

    private val yProperty = (0.0).toProperty()
    var y by yProperty

    init {
        this.xProperty.addListener { _, _, newX ->
            translateX = newX.toDouble() - P0_SIDE / 2
        }
        this.yProperty.addListener { _, _, newY ->
            translateY = newY.toDouble() - P0_SIDE / 2
        }

        fill = Color.TRANSPARENT
        stroke = Color.DARKGRAY
//        super.addEventHandler(MouseEvent.MOUSE_PRESSED) { cursor = Cursor.CROSSHAIR }
//        super.addEventHandler(MouseEvent.MOUSE_DRAGGED) {
//            stroke = Color.BLACK
//            cursor = Cursor.CROSSHAIR
//        }
//        onMouseReleased = EventHandler {
//            cursor = Cursor.DEFAULT
//            stroke = Color.DARKGREY
//        }
//        onMouseEntered = EventHandler {
//            stroke = Color.BLACK
//        }
//        onMouseExited = EventHandler {
//            stroke = Color.DARKGREY
//            cursor = Cursor.DEFAULT
//        }

    }


}
