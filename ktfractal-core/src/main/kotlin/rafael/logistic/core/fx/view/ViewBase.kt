package rafael.logistic.core.fx.view

import javafx.beans.property.IntegerProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.Node
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import rafael.logistic.core.generation.GenerationStatus
import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.core.fx.mapchart.MapChart
import rafael.logistic.core.fx.oneProperty
import tornadofx.*
import java.io.File
import java.util.prefs.Preferences


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
        val spinner: Spinner<Double>,
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
        constructor(spinner: Spinner<Double>, min: Double, max: Double, initialValue: Double, delta: Int = 1) :
                this(spinner, doubleSpinnerValueFactory(min, max, initialValue, 0.1), delta.toProperty())
    }

    // @formatter:off

    override        val root                    :   BorderPane      by fxml("/$fxmlFile.fxml")

    protected       val spnIterations           :   Spinner<Int>    by fxid()
    protected open  val iterationsValueFactory  :   SpinnerValueFactory<Int>
            =   SpinnerValueFactory.IntegerSpinnerValueFactory(100, 1000, 100, 100)

    protected       val chart                   :   C               by fxid()

    protected       val logisticData            =   emptyList<T>().toProperty()

    /**
     * Array com as [Configurações][SpinnerConfigurations] dos [Spinner]s
     */
    protected abstract  val spinnerComponents   :   Array<SpinnerConfigurations>

    // @formatter:on

    private val generationStatusProperty = GenerationStatus.IDLE.toProperty()
    fun generationStatusProperty() = generationStatusProperty as ReadOnlyObjectProperty<GenerationStatus>

    override fun onBeforeShow() {
        initializeControls()
        spnIterations.configureActions(iterationsValueFactory, ::loadData)
        spinnerComponents.forEach { (spinner, factory, deltaProperty) ->
            spinner.configureActions(factory, deltaProperty, ::loadData)
        }

        chart.bind(logisticData)
        chart.generationStatusProperty.bindBidirectional(this.generationStatusProperty)
        root.setOnKeyPressed { event ->
            if (event.isControlDown && event.code == KeyCode.S) {
                exportImage()
            }
        }
        initializeCharts()

        initializeAdditional()

        loadData()
    }

    private fun reloadData(): List<T> {
        this.generationStatusProperty.value = GenerationStatus.CALCULATING
        return refreshData(generator, spnIterations.value)
    }

    protected abstract fun initializeControls()

    protected abstract fun initializeCharts()

    protected abstract fun refreshData(generator: G, iterations: Int): List<T>

    protected abstract fun getImageName(): String

    protected open fun initializeAdditional() {
    }

    protected fun Spinner<Double>.configureSpinner(
        valueFactory: SpinnerValueFactory.DoubleSpinnerValueFactory,
        deltaProperty: IntegerProperty
    ) = this.configureActions(valueFactory, deltaProperty, ::loadData)

    protected fun exportImage() {
        val prefs = Preferences.userRoot().node(this.javaClass.name)
        val imageDir = prefs.get("imageDir", System.getProperty("user.home"))
        val imageName = getImageName() + ".png"

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

    protected fun loadData() {
        this.logisticData.value = reloadData()
    }

}
