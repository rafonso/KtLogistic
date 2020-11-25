package rafael.logistic.map.tent

import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.view.ViewDouble
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.valueToString
import tornadofx.*

class TentMapApp: App(TentMapView::class, Styles::class)

class TentMapView : ViewDouble<TentMapGenerator, TentMapChart>("Tent Map", "TentMap", TentMapGenerator()) {

    // @formatter:off
    private val spnMi           :   Spinner<Double>   by fxid()
    private val deltaMiProperty =   oneProperty()
    private val miValueFactory  =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 2.0, 1.0, maxDelta)
    // @formatter:on

    override fun refreshData(generator: TentMapGenerator, iterations: Int): List<Double> =
            generator.generate(x0Property.value, spnMi.value, iterations)

    override fun initializeAdditional() {
        chart.miProperty.bind(spnMi.valueProperty())
    }

    override fun initializeControlsDouble() {
        spnMi.configureActions(miValueFactory, deltaMiProperty, this::loadData)
    }

    override fun getImageName1(): String = "tent.Mi=${spnMi.valueToString()}"

}
