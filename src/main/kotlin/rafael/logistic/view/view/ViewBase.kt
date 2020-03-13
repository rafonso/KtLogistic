package rafael.logistic.view.view

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import rafael.logistic.generator.IterationGenerator
import rafael.logistic.view.configureActions
import rafael.logistic.view.mapchart.MapChartBase
import tornadofx.*
import java.time.Duration
import java.time.Instant
import java.time.LocalTime

abstract class ViewBase<T, G : IterationGenerator<*, T, *>, C : MapChartBase<T>>(title: String, fxmlFile: String, protected val generator: G) : View(title) {

    // @formatter:off
    override    val root                    :   BorderPane      by fxml("/$fxmlFile.fxml")

    protected   val spnIterations           :   Spinner<Int>    by fxid()
    private     val iterationsValueFactory  =   SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100, 100, 1)

    protected   val chart                   :   C               by fxid()

    protected   val logisticData            =   emptyList<T>().toProperty()
    // @formatter:on

    override fun onBeforeShow() {
//        System.setProperty("java.util.logging.config.file", "/logging.properties")

        spnIterations.configureActions(iterationsValueFactory, ::loadData)
        initializeControls()

        chart.bind(logisticData)
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
        val t0 = LocalTime.now()

        this.logisticData.value = refreshData(generator, spnIterations.value)

        val deltaT = Duration.between(t0, LocalTime.now())
        log.finest("FINEST")
        log.finer("FINER")
        log.fine("FINE")
        log.info("${spnIterations.value} : $deltaT")
    }

}
