package rafael.logistic.core.fx.mapchart

import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.PixelFormat
import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.zeroProperty
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.GenerationStatus
import tornadofx.*
import java.io.File

abstract class CanvasChart<T> : Canvas(), MapChart<T, ByteArray> {

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
            override    val data0Property               =   dataProperty

    private             val mousePositionRealProperty   =   BiDouble(0.0, 0.0).toProperty()

                        val backgroundProperty          =   Color.WHITE.toProperty()

    private             val gc                          :   GraphicsContext = super.getGraphicsContext2D()

    private             val pixelWriter                 :   PixelWriter = gc.pixelWriter

    private             val hProperty                   =   oneProperty()
    protected           val h                           by  hProperty

    private             val wProperty                   =   oneProperty()
    protected           val w                           by  wProperty

    private             val pixelFormat                 =   PixelFormat.getByteRgbInstance()

    private lateinit    var dataGenerator               :   (() -> List<T>)

    private             fun Double.realToCanvasX()      = (this - xMin) / (xMax - xMin) * super.getWidth()

    private             fun Double.canvasToRealX()      = (xMax - xMin) * this / super.getWidth() + xMin

    private             fun Double.realToCanvasY()      = (1 - (this - yMin) / (yMax - yMin)) * super.getHeight()

    private             fun Double.canvasToRealY()      = (yMax - yMin) * (super.getHeight() - this) / super.getHeight() + yMin

    // @formatter:on

    init {
        initialize()
    }

    protected fun initialize() {
        hProperty.bind(super.heightProperty())
        wProperty.bind(super.widthProperty())

        deltaXByPixelProp.bind((xMaxProperty - xMinProperty) / super.widthProperty())
        deltaYByPixelProp.bind((yMaxProperty - yMinProperty) / super.heightProperty())

        this.onMouseMoved = EventHandler { event ->
            mousePositionRealProperty.value = BiDouble(event.x.canvasToRealX(), event.y.canvasToRealY())
        }
        this.onMouseExited = EventHandler { mousePositionRealProperty.value = null }
    }

    override fun prepareBackground(data0: List<T>) {
        gc.clearRect(0.0, 0.0, width, height)

        // Paint background
        gc.fill = backgroundProperty.value
        gc.fillRect(0.0, 0.0, width, height)
    }

    override fun plotData(element: ByteArray) {
        pixelWriter.setPixels(0, 0, this.w, this.h, pixelFormat, element, 0, this.w * 3)
    }

    override fun finalizePlotting() {

        fun plotAxis(x1: Double, y1: Double, x2: Double, y2: Double) {
            gc.stroke = Color.GREY
            gc.lineWidth = 1.0
            gc.strokeLine(x1, y1, x2, y2)
        }

        fun plotHorizontalAxis(xCanvas: Double) = plotAxis(xCanvas, 0.0, xCanvas, super.getHeight())

        fun plotVerticalAxis(yCanvas: Double) = plotAxis(0.0, yCanvas, super.getWidth(), yCanvas)

        if (0.0 in this.xMin..this.xMax) {
            plotHorizontalAxis(0.0.realToCanvasX())
        }
        if (0.0 in this.yMin..this.yMax) {
            plotVerticalAxis(0.0.realToCanvasY())
        }
    }

    override fun mousePositionRealProperty() = mousePositionRealProperty as ReadOnlyObjectProperty<BiDouble>

    override fun bind(dataGenerator: () -> List<T>) {
        this.dataGenerator = dataGenerator
    }

    override fun reloadData() {
        this.dataProperty.value = this.dataGenerator()
    }

    override fun exportImageTo(file: File): Boolean =
        exportImageTo(this, this.w, this.h, file)

}
