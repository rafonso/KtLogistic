package rafael.logistic.core.fx.mapchart

import javafx.beans.binding.Bindings
import javafx.beans.binding.When
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.FlowPane
import rafael.logistic.core.fx.oneProperty
import rafael.logistic.core.fx.zeroProperty
import tornadofx.*

class MouseRealPosNode : FlowPane() {

    // @formatter:off

    private val txtPos                      = TextField("(+0.123456789, -0.98654321)").also { txf ->
        txf.isEditable = false
        txf.prefWidth = 300.0
    }

    private val mousePositionRealProperty   =   MouseRealPos.noMouseRealPos.toProperty()

    private val deltaXByPixelProperty       =   zeroProperty()

    private val deltaYByPixelProperty       =   zeroProperty()

    private val xDigitsProperty             =   oneProperty()

    private val yDigitsProperty             =   oneProperty()

    private val xSignProperty               =   "+".toProperty()

    private val ySignProperty               =   "+".toProperty()

    private val formatProperty              =   Bindings.concat(
        "(%", xSignProperty, ".", xDigitsProperty.asString(), "f," +
                " %", ySignProperty, ".", yDigitsProperty.asString(), "f)"
    )

    private val showXSignProperty           =   SimpleBooleanProperty(this, "showXSign", true)
            var showXSign                   by  showXSignProperty

    private val showYSignProperty           =   SimpleBooleanProperty(this, "showYSign", true)
            var showYSign                   by  showYSignProperty

    // @formatter:on

    init {
        super.getChildren().add(txtPos)

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
        txtPos.text =
            if (mousePositionRealProperty.value.isValid)
                formatProperty.value.format(mousePositionRealProperty.value.x, mousePositionRealProperty.value.y)
            else
                ""
    }

    fun bind(chart: MapChart<*, *>) {
        mousePositionRealProperty.bind(chart.mousePositionRealProperty())
        deltaXByPixelProperty.bind(chart.deltaXByPixelProperty)
        deltaYByPixelProperty.bind(chart.deltaYByPixelProperty)
        chart.addEventHandler(MouseEvent.MOUSE_CLICKED) { event ->
            if (event.button == MouseButton.MIDDLE) {
                Clipboard.getSystemClipboard().putString(txtPos.text)
            }
        }
    }

}
