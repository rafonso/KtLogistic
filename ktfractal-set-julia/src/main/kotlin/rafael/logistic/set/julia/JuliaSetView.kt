package rafael.logistic.set.julia

import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.spinners.DoubleSpinner
import rafael.logistic.core.fx.spinners.valueToString
import rafael.logistic.set.SetView
import tornadofx.*

class JuliaSetApp: App(JuliaSetView::class, Styles::class)

class JuliaSetView : SetView("Julia Set", "JuliaSet", JuliaSetGenerator()) {

    // @formatter:off

    private     val spnCX               :   DoubleSpinner     by  fxid()

    private     val spnCY               :   DoubleSpinner     by  fxid()

    override    val spinnerComponents   =   arrayOf(
        SpinnerConfigurations(spnCX, -LIMIT, LIMIT, LIMIT / 2),
        SpinnerConfigurations(spnCY, -LIMIT, LIMIT, LIMIT / 2),
    )

    // @formatter:on

    override fun getImageName(iterations: Int): String = "julia" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".YMin=${spnYMin.valueToString()}" +
            ".YMax=${spnYMax.valueToString()}" +
            ".CX=${spnCX.valueToString()}" +
            ".CY=${spnCY.valueToString()}" +
            ".Iterations_Dot=${iterations}"

    override fun initializeControls() {
        super.initializeControls()

        super.cXProperty.bind(spnCX.valueProperty())
        super.cYProperty.bind(spnCY.valueProperty())
    }

}
