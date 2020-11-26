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
import rafael.logistic.core.fx.mapchart.MapChart
import tornadofx.*
import java.io.File
import java.util.prefs.Preferences


abstract class ViewBase<T, G : IterationGenerator<*, T, *>, C>(
    title: String,
    fxmlFile: String,
    protected val generator: G
) :
    View(title) where C : MapChart<T, *>, C : Node {

    // @formatter:off

    override        val root                    :   BorderPane      by fxml("/$fxmlFile.fxml")

    protected       val spnIterations           :   Spinner<Int>    by fxid()
    protected open  val iterationsValueFactory  :   SpinnerValueFactory<Int>
            =   SpinnerValueFactory.IntegerSpinnerValueFactory(100, 1000, 100, 100)

    protected       val chart                   :   C               by fxid()

    protected       val logisticData            =   emptyList<T>().toProperty()

    // @formatter:on

    private val generationStatusProperty = GenerationStatus.IDLE.toProperty()
    fun generationStatusProperty() = generationStatusProperty as ReadOnlyObjectProperty<GenerationStatus>

    override fun onBeforeShow() {
        spnIterations.configureActions(iterationsValueFactory, ::loadData)
        initializeControls()

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
    ) = this.configureActions(valueFactory, deltaProperty) { loadData() }

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
