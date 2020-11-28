package rafael.logistic.core.fx.mapchart

import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import rafael.logistic.core.fx.zeroProperty
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.GenerationStatus
import tornadofx.*
import java.io.File

typealias PixelInfo = Triple<Int, Int, Color>

abstract class CanvasChart<T> : Canvas(), MapChart<T, PixelInfo> {

    // @formatter:off

                        val x0Property                  =   zeroProperty()
    private             val x0                          by  x0Property

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

    private             val dataProperty                =   emptyList<T>().toProperty()
    protected           var data                        :   List<T> by dataProperty

    private             val mousePositionRealProperty   =   BiDouble(0.0, 0.0).toProperty()

                        val backgroundProperty          =   Color.WHITE.toProperty()

    protected           val gc                          :   GraphicsContext = super.getGraphicsContext2D()

    protected   fun Double.realToCanvasX() = (this - xMin) / (xMax - xMin) * super.getWidth()

    private     fun Double.canvasToRealX() = (xMax - xMin) * this / super.getWidth() + xMin

    protected   fun Double.realToCanvasY() = (1 - (this - yMin) / (yMax - yMin)) * super.getHeight()

    private     fun Double.canvasToRealY() = (yMax - yMin) * (super.getHeight() - this) / super.getHeight() + yMin

    // @formatter:on

    init {
        deltaXByPixelProp.bind((xMaxProperty - xMinProperty) / super.widthProperty())
        deltaYByPixelProp.bind((yMaxProperty - yMinProperty) / super.heightProperty())

        dataProperty.onChange { refreshData() }

        this.onMouseMoved = EventHandler { event ->
            mousePositionRealProperty.value = BiDouble(event.x.canvasToRealX(), event.y.canvasToRealY())
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

    override fun mousePositionRealProperty() = mousePositionRealProperty as ReadOnlyObjectProperty<BiDouble>

    override fun bind(dataProperty: ReadOnlyObjectProperty<List<T>>, handler: (MapChart<T, *>) -> Unit) {
        this.dataProperty.bind(dataProperty)
        handler(this)
    }

    override fun exportImageTo(file: File): Boolean =
        exportImageTo(file, super.getWidth().toInt(), super.getHeight().toInt()) {
            super.snapshot(null, it)
        }

}
