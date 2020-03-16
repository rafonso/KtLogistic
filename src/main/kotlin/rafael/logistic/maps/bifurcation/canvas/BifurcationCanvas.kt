package rafael.logistic.maps.bifurcation.canvas

import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.geometry.Point2D
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import rafael.logistic.maps.bifurcation.RData
import rafael.logistic.view.GenerationStatus
import rafael.logistic.view.mapchart.MapChart
import tornadofx.*

private const val MAX_RADIUS = 1.0
private const val MIN_RADIUS = 0.4
private const val DELTA_RADIUS = MAX_RADIUS - MIN_RADIUS

class BifurcationCanvas() : Canvas(), MapChart<RData> {

    // @formatter:off

            val     x0Property                  =   (0.0).toProperty()
    private val     x0                          by  x0Property

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

    // @formatter:on

    init {
        dataProperty.onChange { repaint() }
    }

    private fun repaint() {
        this.generationStatusProperty.value = GenerationStatus.PLOTING

        println("%4d -> %10d".format(data.size, data.map { it.values.size }.sum()))

        val gc = super.getGraphicsContext2D()
        gc.clearRect(0.0, 0.0, width, height)
        gc.beginPath()

        // Paint background
        gc.fill = Color.WHITE;
        gc.fillRect(0.0, 0.0, width, height);

        // draw diagonals
        gc.stroke = Color.BLACK;
        gc.moveTo(0.0, 0.0);
        gc.lineTo(width, height);
        gc.moveTo(0.0, height);
        gc.lineTo(width, 0.0);
        gc.stroke();


        this.generationStatusProperty.value = GenerationStatus.IDLE
    }

    override fun mousePositionRealProperty(): ReadOnlyObjectProperty<Point2D> {
        return Point2D(0.0, 0.0).toProperty()
    }

    override fun bind(dataProperty: ReadOnlyObjectProperty<List<RData>>, handler: (MapChart<RData>) -> Unit) {
        this.dataProperty.bind(dataProperty)
        handler(this)
    }


}

/*
    private fun rSequenceToElements(rSequence: RData): Stream<Circle> {
        val rChart = rSequence.r.realToChartX()
        return rSequence.values
                .mapIndexed { i, v -> Pair(v.realToChartY(), i.toDouble() / rSequence.values.size) }
                .parallelStream()
                .map { (xChart, pos) ->
                    Circle(rChart, xChart, (DELTA_RADIUS * pos + MIN_RADIUS)).apply {
                        stroke = getRainbowColor(pos)
                        fill = stroke
                    }
                }
    }

    override fun plotData() {
        if(x0 in myYAxis.lowerBound..myYAxis.lowerBound) {
            highlightP0(myXAxis.lowerBound, x0Property.value)
        }

        // TODO: Está sendo chamado 2 vezes ao redimensionar. Descobrir por quê.
//        Throwable().printStackTrace()

        val circles = data.parallelStream().flatMap { this.rSequenceToElements(it) }.toList()
        background.getChildList()?.addAll(circles)
    }

 */