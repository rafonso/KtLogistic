package rafael.logistic.maps.kaplanyorke

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.geometry.Point2D
import rafael.logistic.core.fx.configureActions
import rafael.logistic.core.fx.view.ViewBi

class KaplanYorkeView : ViewBi<KaplanYorkeGenerator>("Kaplan-Yorke", "KaplanYorke", KaplanYorkeGenerator()) {

    override val iniX0Spinner: Double
        get() = 0.5

    // @formatter:off
    private val spnA            :   Spinner<Double>   by fxid()
    private val deltaAProperty  =   SimpleIntegerProperty(this, "deltaAlpha"    , 1     )
    private val aValueFactory   =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.1, maxDelta)

    // @formatter:on

    override fun refreshData(generator: KaplanYorkeGenerator, iterations: Int): List<Point2D> =
            generator.generate(Point2D(x0Property.value, y0Property.value), spnA.value, iterations)

    override fun initializeControlsBi() {
        spnA.configureActions(aValueFactory, deltaAProperty, this::loadData)
    }

}
