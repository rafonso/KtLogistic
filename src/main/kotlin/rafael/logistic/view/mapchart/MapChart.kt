package rafael.logistic.view.mapchart

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.geometry.Point2D
import rafael.logistic.view.GenerationStatus

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

    fun mousePositionRealProperty(): ReadOnlyObjectProperty<Point2D>

    fun bind(dataProperty: ReadOnlyObjectProperty<List<T>>, handler: (MapChart<T, *>) -> Unit = {})

    fun <E : Event> addEventHandler(eventType: EventType<E>, eventHandler: EventHandler<in E>)

    fun <E : Event> addEventHandler(eventType: EventType<E>, eventHandler: (E) -> Unit) {
        addEventHandler(eventType, EventHandler(eventHandler))
    }

    fun prepareBackground()

    fun dataToElementsToPlot(): List<E>

    fun plotData(elements: List<E>)

    fun refreshData() {
        this.generationStatusProperty.value = GenerationStatus.PLOTTING
        prepareBackground()

        this.generationStatusProperty.value = GenerationStatus.PLOTTING_CONVERT
        val elementsToPlot = dataToElementsToPlot()

        this.generationStatusProperty.value = GenerationStatus.PLOTTING_DRAW
        plotData(elementsToPlot)

        this.generationStatusProperty.value = GenerationStatus.IDLE
    }

}
