package rafael.logistic.core.fx.view

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.Node
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.input.KeyCombination
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import rafael.logistic.core.fx.mapchart.MapChart
import rafael.logistic.core.fx.spinners.DoubleSpinner
import rafael.logistic.core.fx.spinners.IntSpinner
import rafael.logistic.core.fx.spinners.Resetable
import rafael.logistic.core.generation.GenerationStatus
import rafael.logistic.core.generation.GenerationStatusChronometerListener
import rafael.logistic.core.generation.IterationGenerator
import tornadofx.*
import java.io.File
import java.util.logging.LogManager
import kotlin.time.ExperimentalTime


abstract class ViewBase<T, G : IterationGenerator<*, T, *>, C>(
    title: String,
    fxmlFile: String,
    protected val generator: G
) :
    View(title) where C : MapChart<T, *>, C : Node {

    /**
     * Dados de configuração de um [Spinner] do tipo [Double].
     *
     * @property spinner [Spinner] a ser configurado
     * @property factory O [gerador][SpinnerValueFactory.DoubleSpinnerValueFactory] dos valores do Spinner
     * @property decimalPlaces Propriedade relacionada ao [passo][SpinnerValueFactory.DoubleSpinnerValueFactory.amountToStepBy] do Spinner.
     * @constructor Dados de configuração com o Spinner, [gerador][SpinnerValueFactory.DoubleSpinnerValueFactory] e o
     * [passo][SpinnerValueFactory.DoubleSpinnerValueFactory.amountToStepBy]
     */
    protected data class SpinnerConfigurations(
        val spinner: DoubleSpinner,
        val factory: SpinnerValueFactory.DoubleSpinnerValueFactory,
        val decimalPlaces: Int = 1
    ) {

        /**
         * @param spinner [Spinner] a ser configurado
         * @param min Valor mínimo do Spinner
         * @param max Valor máximo do Spinner
         * @param initialValue Valor inicial do Spinner
         * @param decimalPlaces valor inicial do [passo][SpinnerValueFactory.DoubleSpinnerValueFactory.amountToStepBy] do Spinner
         */
        constructor(spinner: DoubleSpinner, min: Double, max: Double, initialValue: Double, decimalPlaces: Int = 1) :
                this(spinner, SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue, 0.1), decimalPlaces)
    }

    // @formatter:off

    override        val root                        :   BorderPane      by fxml("/$fxmlFile.fxml")

    private         val spnIterations               :   IntSpinner    by fxid()
    protected open  val iterationsValueFactory      :   SpinnerValueFactory<Int>
            =   SpinnerValueFactory.IntegerSpinnerValueFactory(100, 2000, 100, 100)
    protected       val iterationsProperty          :   ReadOnlyObjectProperty<Int> =   spnIterations.valueProperty()

    protected       val chart                       :   C               by fxid()

    private         var isReseting                  =   false

    /**
     * Array com as [Configurações][SpinnerConfigurations] dos [Spinner]s
     */
    protected abstract  val spinnerComponents       :   Array<SpinnerConfigurations>

    /**
     * Array indicando os [DoubleSpinner]s e a respectiva propriedade do [chart] que eles ajustam.
     */
    protected abstract  val spinnersChartProperties : Array<Pair<DoubleSpinner, DoubleProperty>>

    // @formatter:on

    private val generationStatusProperty = GenerationStatus.IDLE.toProperty()
    fun generationStatusProperty() = generationStatusProperty as ReadOnlyObjectProperty<GenerationStatus>

    @OptIn(ExperimentalTime::class)
    override fun onBeforeShow() {
        LogManager.getLogManager().readConfiguration(this.javaClass.classLoader.getResourceAsStream("logging.properties"))
        initializeControls()
        spnIterations.initialize(iterationsValueFactory, ::loadData)
        spinnerComponents.forEach { (spinner, factory, deltaProperty) ->
            spinner.initialize(factory, deltaProperty, ::loadData)
        }

        chart.bind { refreshData(generator, iterationsProperty.value) }
        chart.generationStatusProperty.bindBidirectional(this.generationStatusProperty)
        shortcut(KeyCombination.valueOf("Ctrl+S")) { exportImage() }
        shortcut(KeyCombination.valueOf("Ctrl+Home")) { resetControls() }
        this.spinnersChartProperties.forEach { (spinner, property) -> property.bind(spinner.valueProperty()) }
        initializeCharts(iterationsProperty)

        // Adiciona um listener para verificar o tempo gasto em cada um dos GenerationStatus.
        // Entretanto, ele estará ativo apenas se a propriedade do sistema "timer" for passada.
        GenerationStatusChronometerListener.bind(generationStatusProperty())
        initializeAdditional()

        loadData()
    }

    /**
     * Reinicia os controles marcados como [Resetable] a seus respectivos valores iniciais.
     */
    private fun resetControls() {
        // Evita que [MapChart.refreshData] seja chamado a cada componente resetado.
        isReseting = true

        root.top
            .lookupAll(".resetable")
            .map { it as Resetable }
            .forEach(Resetable::resetValue)

        isReseting = false
        loadData()
    }

    protected abstract fun initializeControls()

    protected abstract fun initializeCharts(iterationsProperty: ReadOnlyObjectProperty<Int>)

    protected abstract fun refreshData(generator: G, iterations: Int): List<T>

    /**
     * @param iterations O número de Irterações
     * @return Nome da imagem
     */
    protected abstract fun getImageName(iterations: Int): String

    protected open fun initializeAdditional() {
    }

    protected fun DoubleSpinner.configureSpinner(
        valueFactory: SpinnerValueFactory.DoubleSpinnerValueFactory,
        delta: Int = 1
    ) = this.initialize(valueFactory, delta, ::loadData)

    protected fun exportImage() {
        preferences(this.javaClass.name) {
            val imageDir = get("imageDir", System.getProperty("user.home"))
            val imageName = getImageName(iterationsProperty.value) + ".png"

            val filter = arrayOf(FileChooser.ExtensionFilter("PNG File", listOf("*.png")))
            chooseFile(
                "Export Image",
                filter,
                File(imageDir),
                FileChooserMode.Save,
                super.currentWindow
            ) { this.initialFileName = imageName }
                .firstOrNull()
                ?.let { imageFile ->
                    if (chart.exportImageTo(imageFile)) {
                        log.info("Arquivo salvo em $imageFile")
                        if (imageFile.parent != imageDir) {
                            put("imageDir", imageFile.parent)
                        }
                    }
                }
        }
    }

    /**
     * Notifica [chart] para se atualizar.
     *
     * @param recalculate Se os dados brutos deven ser recalculados ou não.
     */
    protected fun loadData(recalculate: Boolean = true) {
        if (!isReseting) {
            chart.refreshData(recalculate)
        }
    }

}
