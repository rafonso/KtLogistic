package rafael.logistic.map.logistic

import javafx.beans.property.DoubleProperty
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.mapchart.MouseRealPosNode
import rafael.logistic.core.fx.spinners.DoubleSpinner
import rafael.logistic.map.fx.view.ViewDouble
import tornadofx.*

class LogisticMapApp: App(LogisticMapView::class, Styles::class)

class LogisticMapView : ViewDouble<LogisticMapGenerator, LogisticMapChart>("Logistic Map", "LogisticMap", LogisticMapGenerator()) {

    // @formatter:off

    private     val spnR                    :   DoubleSpinner       by fxid()

    private     val txtMouseRealPos         :   MouseRealPosNode    by fxid()

    override    val spinnerComponents       =   arrayOf(SpinnerConfigurations(spnR, 0.0, 4.0, 1.0))

    override    val spinnersChartProperties:Array<Pair<DoubleSpinner, DoubleProperty>> =   arrayOf(Pair(spnR, chart.rProperty))

    // @formatter:on

    override fun refreshData(generator: LogisticMapGenerator, iterations: Int): List<Double> =
            generator.generate(x0Property.value, spnR.value, iterations)

    override fun initializeAdditional() {
        txtMouseRealPos.showXSign = false
        txtMouseRealPos.showYSign = false
        txtMouseRealPos.bind(chart)
    }

    override fun getImageName1(): String = "logistic.R=${spnR.valueToString()}"

}
