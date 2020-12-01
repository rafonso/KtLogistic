package rafael.logistic.map.tent

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.map.fx.view.ViewDouble
import tornadofx.App

class TentMapApp: App(TentMapView::class, Styles::class)

class TentMapView : ViewDouble<TentMapGenerator, TentMapChart>("Tent Map", "TentMap", TentMapGenerator()) {

    // @formatter:off
    private     val spnMi               :   Spinner<Double>   by fxid()

    override    val spinnerComponents   =   arrayOf(SpinnerConfigurations(spnMi, 0.0, 2.0, 1.0))

    // @formatter:on

    override fun refreshData(generator: TentMapGenerator, iterations: Int): List<Double> =
            generator.generate(x0Property.value, spnMi.value, iterations)

    override fun initializeAdditional() {
        chart.miProperty.bind(spnMi.valueProperty())
    }

    override fun getImageName1(): String = "tent.Mi=${spnMi.valueToString()}"

}
