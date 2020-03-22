package rafael.logistic.core.view

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.Node
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import rafael.logistic.core.generation.GenerationStatus
import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.configureActions
import rafael.logistic.core.mapchart.MapChart
import tornadofx.*

abstract class ViewBase<T, G : IterationGenerator<*, T, *>, C>(title: String, fxmlFile: String, protected val generator: G) :
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

    protected open fun initializeAdditional() {
    }

    protected fun loadData() {
        this.logisticData.value = reloadData()
    }

}
