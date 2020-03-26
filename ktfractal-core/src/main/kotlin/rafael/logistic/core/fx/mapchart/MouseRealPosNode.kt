package rafael.logistic.core.fx.mapchart

import javafx.beans.binding.Bindings
import javafx.beans.binding.When
import javafx.beans.property.SimpleBooleanProperty
import rafael.logistic.core.generation.BiDouble
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import tornadofx.*

class MouseRealPosNode : TextField("(+0.123456789, -0.98654321)") {

    private val mousePositionRealProperty = BiDouble(0.0, 0.0).toProperty()

    private val deltaXByPixelProperty = (0.0).toProperty()

    private val deltaYByPixelProperty = (0.0).toProperty()

    private val xDigitsProperty = 1.toProperty()

    private val yDigitsProperty = 1.toProperty()

    private val xSignProperty = "+".toProperty()

    private val ySignProperty = "+".toProperty()

    private val formatProperty = Bindings.concat(
            "(%", xSignProperty, ".", xDigitsProperty.asString(), "f," +
            " %", ySignProperty, ".", yDigitsProperty.asString(), "f)"
    )

    private val showXSignProperty = SimpleBooleanProperty(this, "showXSign", true)
    var showXSign by showXSignProperty

    private val showYSignProperty = SimpleBooleanProperty(this, "showYSign", true)
    var showYSign by showYSignProperty

    init {
        isEditable = false

        deltaXByPixelProperty.onChange { xDigitsProperty.value = posFirstDigit(it) }
        deltaYByPixelProperty.onChange { yDigitsProperty.value = posFirstDigit(it) }

        xSignProperty.bind(When(showXSignProperty).then("+").otherwise(""))
        ySignProperty.bind(When(showYSignProperty).then("+").otherwise(""))

        mousePositionRealProperty.onChange { writePos() }
        formatProperty.onChange { writePos() }
    }

    private fun posFirstDigit(d: Double): Int {

        tailrec fun eval(x: Double, count: Int): Int = if (x > 1.0) count else eval(x * 10, count + 1)

        return eval(d, 0)
    }

    private fun writePos() {
        super.setText(
                if (mousePositionRealProperty.value == null) ""
                else formatProperty.value.format(mousePositionRealProperty.value.x, mousePositionRealProperty.value.y)
        )

    }

    fun bind(chart: MapChart<*, *>) {
        mousePositionRealProperty.bind(chart.mousePositionRealProperty())
        deltaXByPixelProperty.bind(chart.deltaXByPixelProperty)
        deltaYByPixelProperty.bind(chart.deltaYByPixelProperty)
        chart.addEventHandler(MouseEvent.MOUSE_CLICKED) { event ->
            if (event.button == MouseButton.MIDDLE) {
                Clipboard.getSystemClipboard().putString(text)
            }
        }
    }

}
