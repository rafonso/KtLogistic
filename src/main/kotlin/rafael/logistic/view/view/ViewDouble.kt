package rafael.logistic.view.view

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import rafael.logistic.generator.IterationGenerator
import rafael.logistic.view.configureActions
import rafael.logistic.view.iterationchart.IterationChartDouble
import rafael.logistic.view.mapchart.MapChartDouble
import tornadofx.*

abstract class ViewDouble<G : IterationGenerator<Double, *>, C : MapChartDouble>(title: String, fxmlFile: String, generator: G) :
        ViewBase<Double, G, C>(title, fxmlFile, generator) {

    protected val maxDelta = 0.1

    protected open val iniX0Spinner = 0.5

    // @formatter:off
    private     val iterationsChart     :   IterationChartDouble by fxid()

    private     val spnX0               :   Spinner<Double> by fxid()
    private     val deltaX0Property     =   1.toProperty()
    private     val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(
            (chart.xAxis as NumberAxis).lowerBound, (chart.xAxis as NumberAxis).upperBound, iniX0Spinner, maxDelta)
    protected   val x0Property:ReadOnlyObjectProperty<Double>   =   spnX0.valueProperty()
    // @formatter:on

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        initializeControlsDouble()
    }

    override fun initializeCharts() {
        iterationsChart.bind(spnIterations.valueProperty(), logisticData)
    }

    protected abstract fun initializeControlsDouble()

}
