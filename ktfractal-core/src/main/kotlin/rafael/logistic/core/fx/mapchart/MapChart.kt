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

    /**
     * Status da geração do gráfico.
     */
    val generationStatusProperty    : ObjectProperty<GenerationStatus>

    /**
     * Dados brutos que serão usado para plotar o gráfico.
     */
    val data0Property               : ReadOnlyObjectProperty<List<T>>

    // @formatter:on

    /**
     * retorna a posição do maouse correspondente aos dados reais.
     */
    fun mousePositionRealProperty(): ReadOnlyObjectProperty<MouseRealPos>

    /**
     * Seta a função que gera os dados brutos e que será usada por [reloadData].
     *
     * @param dataGenerator função que gera os dados brutos
     */
    fun bind(dataGenerator: () -> List<T>)

    fun <E : Event> addEventHandler(eventType: EventType<E>, eventHandler: EventHandler<in E>)

    fun <E : Event> addEventHandler(eventType: EventType<E>, eventHandler: (E) -> Unit) {
        addEventHandler(eventType, EventHandler(eventHandler))
    }

    /**
     * Recarrega os [dados brutos][data0Property].
     * Corresponde ao [Status][GenerationStatus] [GenerationStatus.CALCULATING].
     */
    fun reloadData()

    /**
     * Prepara o gráfico antes de ser plotado com os dados atuais.
     * Corresponde ao [Status][GenerationStatus] [GenerationStatus.PLOTTING_PREPARING].
     *
     * @param data0 Dados brutos.
     */
    fun prepareBackground(data0: List<T>)

    /**
     * Converte os dados atuais nas entidades a serem usadas na plotagem.
     * Corresponde ao [Status][GenerationStatus] [GenerationStatus.PLOTTING_CONVERT].
     *
     * @param data0 Dados brutos.
     * @return Entidade a ser usada na plotagem.
     */
    fun dataToElementsToPlot(data0: List<T>): E

    /**
     * Executa a Plotagem.
     * Corresponde ao [Status][GenerationStatus] [GenerationStatus.PLOTTING_DRAW].
     *
     * @param element Entidade a serem usadas na plotagem.
     */
    fun plotData(element: E)

    /**
     * Finaliza gráfico, adicionando eventuais detalhes finais. Sua versão `default` faz nada.
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
     *
     * @param recalculate Se o dados brutos, indicados por [data0Property], devem ser atualizados.
     */
    fun refreshData(recalculate: Boolean = true) {
        if (recalculate) {
            this.generationStatusProperty.value = GenerationStatus.CALCULATING
            reloadData()
        }

        this.generationStatusProperty.value = GenerationStatus.PLOTTING_PREPARING
        prepareBackground(this.data0Property.value)

        this.generationStatusProperty.value = GenerationStatus.PLOTTING_CONVERT
        val elementToPlot = dataToElementsToPlot(this.data0Property.value)

        this.generationStatusProperty.value = GenerationStatus.PLOTTING_DRAW
        plotData(elementToPlot)

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
