package rafael.logistic.view.view

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import rafael.logistic.view.IterationGenerator
import rafael.logistic.view.GenerationStatus
import rafael.logistic.view.configureActions
import rafael.logistic.view.mapchart.MapChartBase
import tornadofx.*

abstract class ViewBase<T, G : IterationGenerator<*, T, *>, C : MapChartBase<T>>(title: String, fxmlFile: String, protected val generator: G) : View(title) {

    // @formatter:off
    override    val root                    :   BorderPane      by fxml("/$fxmlFile.fxml")

    protected   val spnIterations           :   Spinner<Int>    by fxid()
    private     val iterationsValueFactory  =   SpinnerValueFactory.IntegerSpinnerValueFactory(100, 1000, 100, 100)

    protected   val chart                   :   C               by fxid()

    protected   val logisticData            =   emptyList<T>().toProperty()
    // @formatter:on

    private val generationStatusProperty    =   GenerationStatus.IDLE.toProperty()
    fun generationStatusProperty()          =   generationStatusProperty as ReadOnlyObjectProperty<GenerationStatus>

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
