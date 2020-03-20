package rafael.logistic.maps.julia.julia

import javafx.scene.control.Spinner
import rafael.logistic.maps.julia.JuliaView
import rafael.logistic.view.configureActions
import rafael.logistic.view.doubleSpinnerValueFactory
import tornadofx.*

class JuliaSetView : JuliaView("Julia Set", "JuliaSet", JuliaSetGenerator()) {

    // @formatter:off

    private     val spnCX               :   Spinner<Double>     by  fxid()
    private     val deltaCXProperty     =   1.toProperty()
    private     val cXValueFactory      =   doubleSpinnerValueFactory(-LIMIT, LIMIT, LIMIT / 2, 0.1)

    private     val spnCY               :   Spinner<Double>     by  fxid()
    private     val deltaCYProperty     =   1.toProperty()
    private     val cYValueFactory      =   doubleSpinnerValueFactory(-LIMIT, LIMIT, LIMIT / 2, 0.1)

    // @formatter:on

    override fun initializeControls() {
        super.initializeControls()

        super.cXProperty.bind(spnCX.valueProperty())
        spnCX.configureActions(cXValueFactory, deltaCXProperty, this::loadData)

        super.cYProperty.bind(spnCY.valueProperty())
        spnCY.configureActions(cYValueFactory, deltaCYProperty, this::loadData)
    }

}