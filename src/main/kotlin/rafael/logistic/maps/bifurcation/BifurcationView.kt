package rafael.logistic.maps.bifurcation

import javafx.scene.chart.NumberAxis
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import rafael.logistic.view.configureActions
import rafael.logistic.view.view.ViewBase
import tornadofx.*

class BifurcationView : ViewBase<RData, BifurcationGenerator, BifurcationChart>("Bifurcation", "Bifurcation", BifurcationGenerator()) {

    // @formatter:off
    private     val spnX0              :   Spinner<Double>  by fxid()
    private     val deltaX0Property     =   1.toProperty()
    private     val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.5, 0.1)

    private     val spnSkip             :   Spinner<Int>    by fxid()
    private     val skipValueFactory    =   SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0, 1)

    private     val spnPixelsSeparation :   Spinner<Int>    by fxid()
    private     val pixelsSeparationValueFactory    =   SpinnerValueFactory.ListSpinnerValueFactory(listOf(0, 1, 2, 4, 10, 50, 100).observable())

    // @formatter:on

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)

        spnSkip.configureActions(skipValueFactory, this::loadData)

        pixelsSeparationValueFactory.value = 10
        spnPixelsSeparation.configureActions(pixelsSeparationValueFactory, this::loadData)
    }

    override fun initializeCharts() {
        val chartParent = chart.parent as Region
        chart.prefWidthProperty().bind(chartParent.widthProperty())
        chart.prefHeightProperty().bind(chartParent.heightProperty())

        chart.x0Property.bind(spnX0.valueProperty())
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
                    rAxis.widthProperty().value.toInt() / (spnPixelsSeparation.value + 1), spnSkip.value, iterations)
        else
            emptyList()
    }


}
