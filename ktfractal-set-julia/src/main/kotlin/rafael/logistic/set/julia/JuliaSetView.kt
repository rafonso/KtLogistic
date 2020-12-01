package rafael.logistic.set.julia

import javafx.scene.control.Spinner
import rafael.logistic.core.fx.Styles
import rafael.logistic.core.fx.doubleSpinnerValueFactory
import rafael.logistic.set.SetView
import tornadofx.App

class JuliaSetApp: App(JuliaSetView::class, Styles::class)

class JuliaSetView : SetView("Julia Set", "JuliaSet", JuliaSetGenerator()) {

    // @formatter:off

    private     val spnCX               :   Spinner<Double>     by  fxid()
    private     val cXValueFactory      =   doubleSpinnerValueFactory(-LIMIT, LIMIT, LIMIT / 2, 0.1)

    private     val spnCY               :   Spinner<Double>     by  fxid()
    private     val cYValueFactory      =   doubleSpinnerValueFactory(-LIMIT, LIMIT, LIMIT / 2, 0.1)

    override    val spinnerComponents   = arrayOf(
        SpinnerComponents(spnCX, cXValueFactory),
        SpinnerComponents(spnCY, cYValueFactory),
    )

    // @formatter:on

    override fun getImageName(): String = "julia" +
            ".XMin=${xMinValueFactory.converter.toString(spnXMin.value)}" +
            ".XMax=${xMaxValueFactory.converter.toString(spnXMax.value)}" +
            ".YMin=${yMinValueFactory.converter.toString(spnYMin.value)}" +
            ".YMax=${yMaxValueFactory.converter.toString(spnYMax.value)}" +
            ".CX=${cXValueFactory.converter.toString(spnCX.value)}" +
            ".CY=${cYValueFactory.converter.toString(spnCY.value)}" +
            ".Iterations_Dot=${spnIterations.value}"

    override fun initializeControls() {
        super.initializeControls()

        super.cXProperty.bind(spnCX.valueProperty())
//        spnCX.configureSpinner(cXValueFactory, deltaCXProperty)

        super.cYProperty.bind(spnCY.valueProperty())
//        spnCY.configureSpinner(cYValueFactory, deltaCYProperty)
    }

}
