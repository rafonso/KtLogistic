package rafael.logistic.core.fx.mapchart

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.embed.swing.SwingFXUtils
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.image.WritableImage
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.GenerationStatus
import java.io.File
import javax.imageio.ImageIO

/**
 *
 */
interface MapChart<T, E> {

    val xMinProperty: DoubleProperty
    val xMin: Double

    val xMaxProperty: DoubleProperty
    val xMax: Double

    val yMinProperty: DoubleProperty
    val yMin: Double

    val yMaxProperty: DoubleProperty
    val yMax: Double

    val deltaXByPixelProperty: ReadOnlyDoubleProperty
    val deltaYByPixelProperty: ReadOnlyDoubleProperty

    val generationStatusProperty: ObjectProperty<GenerationStatus>

    fun mousePositionRealProperty(): ReadOnlyObjectProperty<BiDouble>

    fun bind(dataProperty: ReadOnlyObjectProperty<List<T>>, handler: (MapChart<T, *>) -> Unit = {})

    fun <E : Event> addEventHandler(eventType: EventType<E>, eventHandler: EventHandler<in E>)

    fun <E : Event> addEventHandler(eventType: EventType<E>, eventHandler: (E) -> Unit) {
        addEventHandler(eventType, EventHandler(eventHandler))
    }

    fun prepareBackground()

    fun dataToElementsToPlot(): List<E>

    fun plotData(elements: List<E>)

    fun exportImageTo(file: File): Boolean

    fun finalizePlotting() {

    }

    fun refreshData() {
        this.generationStatusProperty.value = GenerationStatus.PLOTTING_PREPARING
        prepareBackground()

        this.generationStatusProperty.value = GenerationStatus.PLOTTING_CONVERT
        val elementsToPlot = dataToElementsToPlot()

        this.generationStatusProperty.value = GenerationStatus.PLOTTING_DRAW
        plotData(elementsToPlot)

        this.generationStatusProperty.value = GenerationStatus.PLOTTING_FINALIZING
        finalizePlotting()

        this.generationStatusProperty.value = GenerationStatus.IDLE
    }

}

fun exportImageTo(file: File, width: Int, height: Int, snapshotGenerator: (WritableImage) -> WritableImage): Boolean {
    val image = WritableImage(width, height)
    val writableImage = snapshotGenerator(image)
    val renderedImage = SwingFXUtils.fromFXImage(writableImage, null)
    return ImageIO.write(renderedImage, "png", file)
}
