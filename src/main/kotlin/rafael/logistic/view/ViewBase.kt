package rafael.logistic.view

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import rafael.logistic.generator.IterationGenerator
import tornadofx.*

abstract class ViewBase<T, G : IterationGenerator<T, *>>(title: String, fxmlFile: String, protected val generator: G) : View(title) {

    // @formatter:off
    override val root                      :   BorderPane    by fxml("/$fxmlFile.fxml")

    protected val spnIteractions           :   Spinner<Int>  by fxid()
    private   val iterationsValueFactory   =   SpinnerValueFactory.IntegerSpinnerValueFactory(50, 2000, 100, 50)

    protected val logisticData             =   emptyList<T>().toProperty()
    // @formatter:on

    override fun onBeforeShow() {
        spnIteractions.configureActions(iterationsValueFactory, ::loadData)
        initializeControls()
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
        this.logisticData.value = refreshData(generator, spnIteractions.value)
    }

}
