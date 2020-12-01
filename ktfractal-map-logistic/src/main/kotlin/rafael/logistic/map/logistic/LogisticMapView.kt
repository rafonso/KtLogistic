package rafael.logistic.map.logistic

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.valueToString
import rafael.logistic.map.fx.view.ViewDouble
import tornadofx.App

class LogisticMapApp: App(LogisticMapView::class, Styles::class)

class LogisticMapView : ViewDouble<LogisticMapGenerator, LogisticMapChart>("Logistic Map", "LogisticMap", LogisticMapGenerator()) {

    // @formatter:off
    private     val spnR                :   Spinner<Double>     by fxid()
    private     val rValueFactory       =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 4.0, 1.0, maxDelta)

    private     val txtMouseRealPos     :   MouseRealPosNode    by fxid()

    override    val spinnerComponents   =   arrayOf(SpinnerComponents(spnR, rValueFactory))

    // @formatter:on

    override fun refreshData(generator: LogisticMapGenerator, iterations: Int): List<Double> =
            generator.generate(x0Property.value, spnR.value, iterations)

    override fun initializeCharts() {
        super.initializeCharts()

        chart.rProperty.bind(spnR.valueProperty())
    }

    override fun initializeAdditional() {
        txtMouseRealPos.showXSign = false
        txtMouseRealPos.showYSign = false
        txtMouseRealPos.bind(chart)
    }

    override fun getImageName1(): String = "logistic.R=${spnR.valueToString()}"

}
