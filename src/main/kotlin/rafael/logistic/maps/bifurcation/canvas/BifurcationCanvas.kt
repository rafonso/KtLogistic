package rafael.logistic.maps.bifurcation.canvas

import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import rafael.logistic.maps.bifurcation.RData
import rafael.logistic.view.GenerationStatus
import rafael.logistic.view.getRainbowColor
import rafael.logistic.view.mapchart.MapChart
import tornadofx.*
import java.util.stream.Stream
import kotlin.streams.toList

typealias PixelInfo = Triple<Int, Int, Color>

class BifurcationCanvas : Canvas(), MapChart<RData, PixelInfo> {

    // @formatter:off

             val    x0Property                  =   (0.0).toProperty()
    private  val    x0                          by  x0Property

    override val    xMinProperty                =   (0.0).toProperty()
    override val    xMin                        by  xMinProperty

    override val    xMaxProperty                =   (0.0).toProperty()
    override val    xMax                        by  xMaxProperty

    override val    yMinProperty                =   (0.0).toProperty()
    override val    yMin                        by  yMinProperty

    override val    yMaxProperty                =   (0.0).toProperty()
    override val    yMax                        by  yMaxProperty

    private     val deltaXByPixelProp           =   (0.0).toProperty()
    override    val deltaXByPixelProperty       =   deltaXByPixelProp as ReadOnlyDoubleProperty

    private     val deltaYByPixelProp           =   (0.0).toProperty()
    override    val deltaYByPixelProperty       =   deltaYByPixelProp as ReadOnlyDoubleProperty

    override    val generationStatusProperty    =   GenerationStatus.IDLE.toProperty()

    private     val dataProperty                =   emptyList<RData>().toProperty()
    private     var data                        by  dataProperty

    private     val mousePositionRealProperty   =   Point2D(0.0, 0.0).toProperty()

    // @formatter:on

    private fun Double.realToCanvasX() = (this - xMin) / (xMax - xMin) * super.getWidth()

    private fun Double.canvasToRealX() = (xMax - xMin) * this / super.getWidth() + xMin

    private fun Double.realToCanvasY() = (1 - (this - yMin) / (yMax - yMin)) * super.getHeight()

    private fun Double.canvasToRealY() = (yMax - yMin) * (super.getHeight() - this) / super.getHeight() + yMin

    private val gc = super.getGraphicsContext2D()

    init {
        deltaXByPixelProp.bind((xMaxProperty - xMinProperty) / super.widthProperty())
        deltaYByPixelProp.bind((yMaxProperty - yMinProperty) / super.heightProperty())

        dataProperty.onChange { refreshData() }

        this.onMouseMoved = EventHandler { event ->
            mousePositionRealProperty.value = Point2D(event.x.canvasToRealX(), event.y.canvasToRealY())
        }
        this.onMouseExited = EventHandler { mousePositionRealProperty.value = null }
    }

    private fun rSequenceToCoordinates(rSequence: RData): Stream<PixelInfo> {
        val rChart = rSequence.r.realToCanvasX().toInt()
        val size = rSequence.values.size

        return rSequence.values
                .mapIndexed { i, v -> Pair(v.realToCanvasY().toInt(), i.toDouble() / size) }
                .parallelStream()
                .map { Triple(rChart, it.first, getRainbowColor(it.second)) }
    }

    override fun prepareBackground() {
        gc.clearRect(0.0, 0.0, width, height)

        // Paint background
        gc.fill = Color.WHITE
        gc.fillRect(0.0, 0.0, width, height)
    }

    override fun dataToElementsToPlot() = data
            .parallelStream()
            .flatMap(this::rSequenceToCoordinates).toList()

    override fun plotData(elements: List<PixelInfo>) {
        val pixelWriter = gc.pixelWriter
        elements.forEach { (x, y, c) -> pixelWriter.setColor(x, y, c) }
    }

    override fun mousePositionRealProperty() = mousePositionRealProperty as ReadOnlyObjectProperty<Point2D>

    override fun bind(dataProperty: ReadOnlyObjectProperty<List<RData>>, handler: (MapChart<RData, *>) -> Unit) {
        this.dataProperty.bind(dataProperty)
        handler(this)
    }

}
