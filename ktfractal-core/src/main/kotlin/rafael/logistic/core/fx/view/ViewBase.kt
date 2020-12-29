package rafael.logistic.core.fx.view

import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.Node
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import rafael.logistic.core.fx.mapchart.MapChart
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.spinners.DoubleSpinner
import rafael.logistic.core.fx.spinners.IntSpinner
import rafael.logistic.core.fx.spinners.doubleSpinnerValueFactory
import rafael.logistic.core.generation.GenerationStatus
import rafael.logistic.core.generation.GenerationStatusChronometerListener
import rafael.logistic.core.generation.IterationGenerator
import tornadofx.*
import java.io.File
import java.util.prefs.Preferences
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
     * @property deltaProperty Propriedade relacionada ao [passo][SpinnerValueFactory.DoubleSpinnerValueFactory.amountToStepBy] do Spinner.
     * @constructor Dados de configuração com o Spinner, [gerador][SpinnerValueFactory.DoubleSpinnerValueFactory] e o
     * [passo][SpinnerValueFactory.DoubleSpinnerValueFactory.amountToStepBy]
     */
    protected data class SpinnerConfigurations(
        val spinner: DoubleSpinner,
        val factory: SpinnerValueFactory.DoubleSpinnerValueFactory,
        val deltaProperty: IntegerProperty = oneProperty()
    ) {

        /**
         * @param spinner [Spinner] a ser configurado
         * @param min Valor mínimo do Spinner
         * @param max Valor máximo do Spinner
         * @param initialValue Valor inicial do Spinner
         * @param delta valor inicial do [passo][SpinnerValueFactory.DoubleSpinnerValueFactory.amountToStepBy] do Spinner
         */
        constructor(spinner: DoubleSpinner, min: Double, max: Double, initialValue: Double, delta: Int = 1) :
                this(spinner, doubleSpinnerValueFactory(min, max, initialValue, 0.1), delta.toProperty())
    }

    // @formatter:off

    override        val root                        :   BorderPane      by fxml("/$fxmlFile.fxml")

    private         val spnIterations               :   IntSpinner    by fxid()
    protected open  val iterationsValueFactory      :   SpinnerValueFactory<Int>
            =   SpinnerValueFactory.IntegerSpinnerValueFactory(100, 2000, 100, 100)
    protected       val iterationsProperty          :   ReadOnlyObjectProperty<Int> =   spnIterations.valueProperty()

    protected       val chart                       :   C               by fxid()

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

    @ExperimentalTime
    override fun onBeforeShow() {
        initializeControls()
        spnIterations.configureActions(iterationsValueFactory, ::loadData)
        spinnerComponents.forEach { (spinner, factory, deltaProperty) ->
            spinner.configureActions(factory, deltaProperty, ::loadData)
        }

        chart.bind { refreshData(generator, iterationsProperty.value) }
        chart.generationStatusProperty.bindBidirectional(this.generationStatusProperty)
        root.setOnKeyPressed { event ->
            if (event.isControlDown && event.code == KeyCode.S) {
                exportImage()
            }
        }
        this.spinnersChartProperties.forEach { (spinner, property) -> property.bind(spinner.valueProperty()) }
        initializeCharts(iterationsProperty)

        // Adiciona um listener para verificar o tempo gasto em cada um dos GenerationStatus.
        // Entretanto, ele estará ativo apenas se a propriedade do sistema "timer" for passada.
        GenerationStatusChronometerListener.bind(generationStatusProperty())
        initializeAdditional()

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
        deltaProperty: IntegerProperty
    ) = this.configureActions(valueFactory, deltaProperty, ::loadData)

    protected fun exportImage() {
        val prefs = Preferences.userRoot().node(this.javaClass.name)
        val imageDir = prefs.get("imageDir", System.getProperty("user.home"))
        val imageName = getImageName(iterationsProperty.value) + ".png"

        chooseFile(
            "Export Image",
            arrayOf(FileChooser.ExtensionFilter("PNG File", listOf("*.png"))),
            File(imageDir),
            FileChooserMode.Save,
            super.currentWindow
        ) {
            this.initialFileName = imageName
        }.firstOrNull()?.let { imageFile ->
            if (chart.exportImageTo(imageFile)) {
                println("Arquivo salvo em $imageFile")
                if (imageFile.parent != imageDir) {
                    prefs.put("imageDir", imageFile.parent)
                }
            }
        }
    }

    protected fun loadData(recalculate: Boolean = true) {
        chart.refreshData(recalculate)
    }

}
