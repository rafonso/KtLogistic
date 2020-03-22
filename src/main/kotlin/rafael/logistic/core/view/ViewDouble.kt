package rafael.logistic.core.view

import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.Region
import rafael.logistic.core.generation.IterationGenerator
import rafael.logistic.core.configureActions
import rafael.logistic.core.iterationchart.IterationChartDouble
import rafael.logistic.core.mapchart.MapChartDouble
import tornadofx.*

abstract class ViewDouble<G : IterationGenerator<*, Double, *>, C : MapChartDouble>(title: String, fxmlFile: String, generator: G) :
        ViewBase<Double, G, C>(title, fxmlFile, generator) {

    protected val maxDelta = 0.1

    private val iniX0Spinner = 0.5

    // @formatter:off
    private     val iterationsChart     :   IterationChartDouble by fxid()

    private     val spnX0               :   Spinner<Double> by fxid()
    private     val deltaX0Property     =   1.toProperty()
    private     val x0ValueFactory      =   SpinnerValueFactory.DoubleSpinnerValueFactory(
            (chart.xAxis as NumberAxis).lowerBound, (chart.xAxis as NumberAxis).upperBound, iniX0Spinner, maxDelta)
    protected   val x0Property:ReadOnlyObjectProperty<Double>   =   spnX0.valueProperty()

//    private     val p0XProperty          =
    // @formatter:on

    override fun initializeControls() {
        spnX0.configureActions(x0ValueFactory, deltaX0Property, this::loadData)
        initializeControlsDouble()
    }

    override fun initializeCharts() {
        val chartParent = chart.parent as Region
        chart.prefWidthProperty().bind(Bindings.min(chartParent.heightProperty(), chartParent.widthProperty()))
        iterationsChart.bind(spnIterations.valueProperty(), logisticData)
//        chart.square.xProperty.asObject().bindBidirectional(x0ValueFactory.valueProperty()) // as Property<Number>)
    }

    protected abstract fun initializeControlsDouble()

}