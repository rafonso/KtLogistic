package rafael.logistic.view.experimental.template

import javafx.scene.control.CheckBox
import javafx.geometry.Point2D
import rafael.logistic.view.mapchart.MouseRealPosNode
import rafael.logistic.view.view.ViewBi
import tornadofx.*

class TemplateView : ViewBi<TemplateGenerator>("Template", "Template", TemplateGenerator()) {

    // @formatter:off
    private val chbPinY         :   CheckBox            by  fxid()

    private val txtMouseRealPos :   MouseRealPosNode    by fxid()
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

    override fun refreshData(generator: TemplateGenerator, iterations: Int): List<Point2D> =
            generator.generate(Point2D(x0Property.value, y0Property.value), super.minX0Spinner, super.maxX0Spinner, iterations)

    override fun initializeAdditional() {
        txtMouseRealPos.bind(chart)
    }

}
