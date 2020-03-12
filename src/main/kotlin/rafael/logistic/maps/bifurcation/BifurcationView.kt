package rafael.logistic.maps.bifurcation

import javafx.scene.chart.NumberAxis
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.view.configureActions
import rafael.logistic.view.view.ViewBase
import tornadofx.*

class BifurcationView : ViewBase<RData, BifurcationGenerator, BifurcationChart>("Bifurcation", "Bifurcation", BifurcationGenerator()) {

    // @formatter:off
    private     val spnX0              :   Spinner<Double>  by fxid()
    private     val deltaX0Property     =   1.toProperty()
    private     val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, 0.1)

    private     val spnSkip             :   Spinner<Int>    by fxid()
    private     val skipValueFactory    =   SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1)

    // @formatter:on

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        chart.x0Property.bind(spnX0.valueProperty())

        spnSkip.configureActions(skipValueFactory, this::loadData)
    }

    override fun initializeCharts() {
        with(super.chart.xAxis as NumberAxis) {
            widthProperty().onChange { w ->
                val rStep = (upperBound - lowerBound) / w
                println("Width $w\tStep $rStep")
                loadData()
            }
        }
    }

    override fun refreshData(generator: BifurcationGenerator, iterations: Int): List<RData> {
        val rAxis = (super.chart.xAxis as NumberAxis)

        return if (rAxis.widthProperty().value > 0)
            return generator.generate(spnX0.value, rAxis.lowerBound, rAxis.upperBound,
                    rAxis.widthProperty().value.toInt() / 10, spnSkip.value, iterations)
        else
            emptyList()
    }


}
