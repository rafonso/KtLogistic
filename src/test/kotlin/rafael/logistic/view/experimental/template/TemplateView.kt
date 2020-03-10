package rafael.logistic.view.experimental.template

import javafx.scene.control.CheckBox
import rafael.logistic.generator.BiPoint
import rafael.logistic.view.view.ViewBi
import tornadofx.*

class TemplateView : ViewBi<TemplateGenerator>("Template", "Template", TemplateGenerator()) {

//    override val maxDelta: Double
//        get() = 1.0
//
//    override val maxX0Spinner: Double
//        get() = 10.0
//
//    override val minX0Spinner: Double
//        get() = -maxX0Spinner
//
//    override val maxY0Spinner: Double
//        get() = maxX0Spinner
//
//    override val minY0Spinner: Double
//        get() = -maxY0Spinner


    // @formatter:off
    private val chbPinY :   CheckBox    by  fxid()
    // @formatter:on

    override fun initializeControlsBi() {
        spnY0.disableWhen { chbPinY.selectedProperty() }
        spnY0.disableProperty().onChange { disable ->
            if(disable) {
                spnY0.valueFactory.value = 0.0
            } else {
                spnY0.requestFocus()
            }
        }
    }

    override fun refreshData(generator: TemplateGenerator, iterations: Int): List<BiPoint> =
            generator.generate(BiPoint(x0Property.value, y0Property.value), super.minX0Spinner, super.maxX0Spinner, iterations)

}
