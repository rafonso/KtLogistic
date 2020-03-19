package rafael.logistic.view.mapchart

import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import rafael.logistic.view.GenerationStatus
import tornadofx.*

typealias PixelInfo = Triple<Int, Int, Color>

abstract class CanvasChart<T> : Canvas(), MapChart<T, PixelInfo> {

    // @formatter:off

                val x0Property                  =   (0.0).toProperty()
    private     val x0                          by  x0Property

    override    val xMinProperty                =   (0.0).toProperty()
    override    val xMin                        by  xMinProperty

    override    val xMaxProperty                =   (0.0).toProperty()
    override    val xMax                        by  xMaxProperty

    override    val yMinProperty                =   (0.0).toProperty()
    override    val yMin                        by  yMinProperty

    override    val yMaxProperty                =   (0.0).toProperty()
    override    val yMax                        by  yMaxProperty

    private     val deltaXByPixelProp           =   (0.0).toProperty()
    override    val deltaXByPixelProperty       =   deltaXByPixelProp as ReadOnlyDoubleProperty

    private     val deltaYByPixelProp           =   (0.0).toProperty()
    override    val deltaYByPixelProperty       =   deltaYByPixelProp as ReadOnlyDoubleProperty

    override    val generationStatusProperty    =   GenerationStatus.IDLE.toProperty()

    private     val dataProperty                =   emptyList<T>().toProperty()
    protected   var data :List<T>               by  dataProperty

    private     val mousePositionRealProperty   =   Point2D(0.0, 0.0).toProperty()

                val backgroundProperty          =   Color.WHITE.toProperty()

    // @formatter:on

    protected fun Double.realToCanvasX() = (this - xMin) / (xMax - xMin) * super.getWidth()

    private fun Double.canvasToRealX() = (xMax - xMin) * this / super.getWidth() + xMin

    protected fun Double.realToCanvasY() = (1 - (this - yMin) / (yMax - yMin)) * super.getHeight()

    private fun Double.canvasToRealY() = (yMax - yMin) * (super.getHeight() - this) / super.getHeight() + yMin

    protected val gc: GraphicsContext = super.getGraphicsContext2D()

    init {
        deltaXByPixelProp.bind((xMaxProperty - xMinProperty) / super.widthProperty())
        deltaYByPixelProp.bind((yMaxProperty - yMinProperty) / super.heightProperty())

        dataProperty.onChange { refreshData() }

        this.onMouseMoved = EventHandler { event ->
            mousePositionRealProperty.value = Point2D(event.x.canvasToRealX(), event.y.canvasToRealY())
        }
        this.onMouseExited = EventHandler { mousePositionRealProperty.value = null }
    }

    override fun prepareBackground() {
        gc.clearRect(0.0, 0.0, width, height)

        // Paint background
        gc.fill = backgroundProperty.value
        gc.fillRect(0.0, 0.0, width, height)
    }

    override fun plotData(elements: List<PixelInfo>) {
        val pixelWriter = gc.pixelWriter
        elements.forEach { (x, y, c) -> pixelWriter.setColor(x, y, c) }
    }

    override fun mousePositionRealProperty() = mousePositionRealProperty as ReadOnlyObjectProperty<Point2D>

    override fun bind(dataProperty: ReadOnlyObjectProperty<List<T>>, handler: (MapChart<T, *>) -> Unit) {
        this.dataProperty.bind(dataProperty)
        handler(this)
    }

}
