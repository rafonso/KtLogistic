package rafael.logistic.view.mapchart

import javafx.beans.binding.Bindings
import javafx.geometry.Point2D
import javafx.scene.control.TextField
import javafx.scene.text.Font
import tornadofx.*

class MouseRealPosNode() : TextField("(+0.123456789, -0.98654321)") {

    private val mousePositionRealProperty = Point2D(0.0, 0.0).toProperty()

    private val deltaXByPixelProperty = (0.0).toProperty()

    private val deltaYByPixelProperty = (0.0).toProperty()

    private val xDigitsProperty = 1.toProperty()

    private val yDigitsProperty = 1.toProperty()

    private val formatProperty = Bindings.concat("(%+.", xDigitsProperty.asString(), "f, %+.", yDigitsProperty.asString(), "f)")

    init {
        font = Font.font("Consolas", 12.0)
        insets(5.0)
        prefWidth = 200.0
        isEditable = false

        deltaXByPixelProperty.onChange { xDigitsProperty.value = posFirstDigit(it) }
        deltaYByPixelProperty.onChange { yDigitsProperty.value = posFirstDigit(it) }

        mousePositionRealProperty.onChange { writePos() }
        formatProperty.onChange { writePos() }
    }

    private fun posFirstDigit(d: Double): Int {

        tailrec fun eval(x: Double, count: Int): Int = if (x > 1.0) count else eval(x * 10, count + 1)

        return eval(d, 0)
    }

    private fun writePos() {
        super.setText(formatProperty.value.format(mousePositionRealProperty.value.x, mousePositionRealProperty.value.y))
    }

    fun bind(chart: MapChartBase<*>) {
        mousePositionRealProperty.bind(chart.mousePositionRealProperty())
        deltaXByPixelProperty.bind(chart.deltaXByPixelProperty)
        deltaYByPixelProperty.bind(chart.deltaYByPixelProperty)
    }

}
