package rafael.logistic.map.logistic

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.view.ViewDouble
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.valueToString
import tornadofx.App

class LogisticMapApp: App(LogisticMapView::class, Styles::class)

class LogisticMapView : ViewDouble<LogisticMapGenerator, LogisticMapChart>("Logistic Map", "LogisticMap", LogisticMapGenerator()) {

    // @formatter:off
    private val spnR            :   Spinner<Double>     by fxid()
    private val deltaRProperty  =   SimpleIntegerProperty(this, "deltaR"    , 1     )
    private val rValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 4.0, 1.0, maxDelta)

    private val txtMouseRealPos :   MouseRealPosNode    by fxid()
    // @formatter:on

    override fun refreshData(generator: LogisticMapGenerator, iterations: Int): List<Double> =
            generator.generate(x0Property.value, spnR.value, iterations)

    override fun initializeControlsDouble() {
        spnR.configureActions(rValueFactory, deltaRProperty, this::loadData)
    }

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
