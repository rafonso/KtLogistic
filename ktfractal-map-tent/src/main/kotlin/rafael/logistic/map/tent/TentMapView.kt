package rafael.logistic.map.tent

import rafael.logistic.core.fx.DoubleSpinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.map.fx.view.ViewDouble
import tornadofx.App

class TentMapApp: App(TentMapView::class, Styles::class)

class TentMapView : ViewDouble<TentMapGenerator, TentMapChart>("Tent Map", "TentMap", TentMapGenerator()) {

    // @formatter:off

    private     val spnMi                   :   DoubleSpinner   by fxid()

    override    val spinnerComponents       =   arrayOf(SpinnerConfigurations(spnMi, 0.0, 2.0, 1.0))

    override    val spinnersChartProperties =   arrayOf(Pair(spnMi, chart.miProperty))

    // @formatter:on

    override fun refreshData(generator: TentMapGenerator, iterations: Int): List<Double> =
            generator.generate(x0Property.value, spnMi.value, iterations)

    override fun getImageName1(): String = "tent.Mi=${spnMi.valueToString()}"

}
