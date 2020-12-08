package rafael.logistic.core.fx.mapchart

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.embed.swing.SwingFXUtils
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Node
import javafx.scene.image.WritableImage
import rafael.logistic.core.generation.BiDouble
import rafael.logistic.core.generation.GenerationStatus
import java.io.File
import javax.imageio.ImageIO

/**
 * Interface básica dos gráficos.
 *
 * @param T Tipo do dado original
 * @param E Tipo da classe que será usada ao plotar o gráfico
 */
interface MapChart<T, E> {

    // @formatter:off

    val xMinProperty                : DoubleProperty
    val xMin                        : Double

    val xMaxProperty                : DoubleProperty
    val xMax                        : Double

    val yMinProperty                : DoubleProperty
    val yMin                        : Double

    val yMaxProperty                : DoubleProperty
    val yMax                        : Double

    val deltaXByPixelProperty       : ReadOnlyDoubleProperty
    val deltaYByPixelProperty       : ReadOnlyDoubleProperty

    val generationStatusProperty    : ObjectProperty<GenerationStatus>

    // @formatter:on

    fun mousePositionRealProperty(): ReadOnlyObjectProperty<BiDouble>

    fun bind(dataProperty: ReadOnlyObjectProperty<List<T>>, handler: (MapChart<T, *>) -> Unit = {})

    fun <E : Event> addEventHandler(eventType: EventType<E>, eventHandler: EventHandler<in E>)

    fun <E : Event> addEventHandler(eventType: EventType<E>, eventHandler: (E) -> Unit) {
        addEventHandler(eventType, EventHandler(eventHandler))
    }

    /**
     * Prepara o gráfico antes de ser plotado com os dados atuais.
     * Corresponde ao [Status][GenerationStatus] [GenerationStatus.PLOTTING_PREPARING].
     */
    fun prepareBackground()

    /**
     * Converte os dados atuais nas entidades a serem usadas na plotagem.
     * Corresponde ao [Status][GenerationStatus] [GenerationStatus.PLOTTING_CONVERT].
     *
     * @return [Array] das entidades a serem usadas na plotagem.
     */
    fun dataToElementsToPlot(): Array<E>

    /**
     * Executa a Plotagem.
     * Corresponde ao [Status][GenerationStatus] [GenerationStatus.PLOTTING_DRAW].
     *
     * @param elements [Array] das entidades a serem usadas na plotagem.
     */
    fun plotData(elements: Array<E>)

    /**
     * Finaliza gráfico, adicionando eventuais detalhes finais.
     * Corresponde ao [Status][GenerationStatus] [GenerationStatus.PLOTTING_FINALIZING].
     */
    fun finalizePlotting() {

    }

    /**
     * Exporta conteúdo do gráfico para um arquivo do tipo PNG.
     *
     * @param file Arquivo PNG onde a imagem será armazenada.
     * @return Se a imagem foi gerada.
     */
    fun exportImageTo(file: File): Boolean

    /**
     * Atualiza um gráfico quando os dados são atualizados.
     */
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

/**
 * Exporta o conteúdo de um [MapChart] para um arquivo tipo PNG.
 *
 * @param node [Node] de origem da imagem, correspondendo ao [MapChart]
 * @param width Largura da imagem
 * @param height Altura da imagem
 * @param file Arquivo PNG onde a imagem será armazenada.
 * @return Se a imagem foi gerada.
 */
fun exportImageTo(node: Node, width: Int, height: Int, file: File): Boolean {
    val image = WritableImage(width, height)
    val writableImage = node.snapshot(null, image)
    val renderedImage = SwingFXUtils.fromFXImage(writableImage, null)
    return ImageIO.write(renderedImage, "png", file)
}
