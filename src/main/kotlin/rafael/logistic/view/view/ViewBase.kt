package rafael.logistic.view.view

import javafx.beans.binding.Bindings
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import rafael.logistic.generator.IterationGenerator
import rafael.logistic.view.configureActions
import rafael.logistic.view.mapchart.MapChartBase
import tornadofx.*

abstract class ViewBase<T, G : IterationGenerator<T, *>, C : MapChartBase<T>>(title: String, fxmlFile: String, protected val generator: G) : View(title) {

    // @formatter:off
    override    val root                    :   BorderPane      by fxml("/$fxmlFile.fxml")

    protected   val spnIterations           :   Spinner<Int>    by fxid()
    private     val iterationsValueFactory  =   SpinnerValueFactory.IntegerSpinnerValueFactory(100, 5000, 100, 100)

    protected   val chart                   :   C               by fxid()

    protected   val logisticData            =   emptyList<T>().toProperty()
    // @formatter:on

    override fun onBeforeShow() {
        spnIterations.configureActions(iterationsValueFactory, ::loadData)
        initializeControls()

        chart.bind(logisticData)
        val chartParent = chart.parent as Region
        chart.prefWidthProperty().bind(Bindings.min(chartParent.heightProperty(), chartParent.widthProperty()))
        initializeCharts()

        initializeAdditional()

        loadData()
    }

    protected abstract fun initializeControls()

    protected abstract fun initializeCharts()

    protected abstract fun refreshData(generator: G, iterations: Int): List<T>

    protected open fun initializeAdditional() {
    }

    protected fun loadData() {
        this.logisticData.value = refreshData(generator, spnIterations.value)
    }

}
