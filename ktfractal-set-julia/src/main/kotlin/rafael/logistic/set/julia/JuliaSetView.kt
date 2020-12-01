package rafael.logistic.set.julia

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.valueToString
import rafael.logistic.set.SetView
import tornadofx.App

class JuliaSetApp: App(JuliaSetView::class, Styles::class)

class JuliaSetView : SetView("Julia Set", "JuliaSet", JuliaSetGenerator()) {

    // @formatter:off

    private     val spnCX               :   Spinner<Double>     by  fxid()

    private     val spnCY               :   Spinner<Double>     by  fxid()

    override    val spinnerComponents   =   arrayOf(
        SpinnerConfigurations(spnCX, -LIMIT, LIMIT, LIMIT / 2),
        SpinnerConfigurations(spnCY, -LIMIT, LIMIT, LIMIT / 2),
    )

    // @formatter:on

    override fun getImageName(): String = "julia" +
            ".XMin=${spnXMin.valueToString()}" +
            ".XMax=${spnXMax.valueToString()}" +
            ".YMin=${spnYMin.valueToString()}" +
            ".YMax=${spnYMax.valueToString()}" +
            ".CX=${spnCX.valueToString()}" +
            ".CY=${spnCY.valueToString()}" +
            ".Iterations_Dot=${spnIterations.value}"

    override fun initializeControls() {
        super.initializeControls()

        super.cXProperty.bind(spnCX.valueProperty())
//        spnCX.configureSpinner(cXValueFactory, deltaCXProperty)

        super.cYProperty.bind(spnCY.valueProperty())
//        spnCY.configureSpinner(cYValueFactory, deltaCYProperty)
    }

}
