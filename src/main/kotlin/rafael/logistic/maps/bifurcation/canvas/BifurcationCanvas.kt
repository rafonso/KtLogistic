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

private const val MAX_RADIUS = 1.0
private const val MIN_RADIUS = 0.4
private const val DELTA_RADIUS = MAX_RADIUS - MIN_RADIUS

class BifurcationCanvas() : Canvas(), MapChart<RData> {

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

    init {
        deltaXByPixelProp.bind((xMaxProperty - xMinProperty) / super.widthProperty())
        deltaYByPixelProp.bind((yMaxProperty - yMinProperty) / super.heightProperty())

        dataProperty.onChange { repaint() }

        this.onMouseMoved = EventHandler { event ->
            mousePositionRealProperty.value = Point2D(event.x.canvasToRealX(), event.y.canvasToRealY())
        }
        this.onMouseExited = EventHandler { mousePositionRealProperty.value = null }
    }

    private fun rSequenceToCoordinates(rSequence: RData): Stream<Triple<Int, Int, Color>> {
        val rChart = rSequence.r.realToCanvasX().toInt()
        return rSequence.values
                .mapIndexed { i, v -> Pair(v.realToCanvasY().toInt(), i.toDouble() / rSequence.values.size) }
                .parallelStream()
                .map { (xChart, pos) -> Triple(rChart, xChart, getRainbowColor(pos)) }
    }

    private fun repaint() {
        this.generationStatusProperty.value = GenerationStatus.PLOTING

        val gc = super.getGraphicsContext2D()
        gc.clearRect(0.0, 0.0, width, height)

        // Paint background
        gc.fill = Color.WHITE;
        gc.fillRect(0.0, 0.0, width, height);

        val coordinates = data.parallelStream().flatMap(this::rSequenceToCoordinates).toList()
        val pixelWriter = gc.pixelWriter
        coordinates.forEach { (x, y, c) -> pixelWriter.setColor(x, y, c) }

        this.generationStatusProperty.value = GenerationStatus.IDLE
    }

    override fun mousePositionRealProperty() = mousePositionRealProperty as ReadOnlyObjectProperty<Point2D>

    override fun bind(dataProperty: ReadOnlyObjectProperty<List<RData>>, handler: (MapChart<RData>) -> Unit) {
        this.dataProperty.bind(dataProperty)
        handler(this)
    }


}
